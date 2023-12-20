package oppen.smolicon

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.drawable.Drawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.preference.PreferenceManager
import oppen.smolicon.databinding.ActivityIconBinding
import oppen.smolicon.view.ColourMatrix
import oppen.smolicon.view.ScreenMatrix
import java.io.FileOutputStream
import java.text.SimpleDateFormat
import java.util.*
import java.io.ByteArrayOutputStream

const val CREATE_FILE_REQ = 1

class IconActivity : AppCompatActivity() {

    private lateinit var binding: ActivityIconBinding
    private val model: IconViewModel by viewModels()

    private var currentColourDrawable: Drawable? = null
    private lateinit var screenMatrix: ScreenMatrix
    private var saveBitmap: Bitmap? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityIconBinding.inflate(layoutInflater)
        binding.model = model
        setContentView(binding.root)

        currentColourDrawable = ResourcesCompat.getDrawable(resources, R.drawable.shape_current_colour_circle, null)

        screenMatrix = ScreenMatrix(binding.skissScreenMatrix){ colour ->
            setColour(colour)
        }

        restore()

        screenMatrix.start()

        model.events.observe(this){ event ->
            when(event){
                Event.ToggleGrid -> screenMatrix.toggleGrid()
                Event.ToggleEraser -> screenMatrix.toggleEraser()
                Event.ToggleDropper -> screenMatrix.toggleDropper()
                Event.ToggleFill -> screenMatrix.toggleFill()
                Event.EraseAll -> screenMatrix.eraseAll()
                Event.Info -> InfoDialog(this).show()
                Event.ColourPicker -> ColourPicker(this, screenMatrix.inkColour){ colour ->
                    setColour(colour)
                }.show()
                Event.Save -> {
                    IconFactory.create(screenMatrix.pixels){ bitmap ->
                        println("Bitmap ready...")
                        saveBitmap = bitmap
                        createSaveFile()
                    }
                }

            }
        }

        binding.eraseButton.setOnLongClickListener {
            model.eraseAll()
            true
        }

        val colorGridBackground = ContextCompat.getColor(this, R.color.grid_background)

        val colourMatrix = ColourMatrix(binding.skissColourMatrix, colorGridBackground, object: ColourMatrix.Listener{
            override fun onColour(colour: String) {
                setColour(colour)
            }
        })
        colourMatrix.start()
    }

    private fun setColour(colour: String?){
        if(colour == null) return
        screenMatrix.setColour(colour)
        currentColourDrawable?.setTint(Color.parseColor(colour))
        binding.currentColourButton.setImageDrawable(currentColourDrawable)
    }

    private fun createSaveFile() {
        val intent = Intent(Intent.ACTION_CREATE_DOCUMENT).apply {
            addCategory(Intent.CATEGORY_OPENABLE)
            type = "image/png"
            val dateLabel: String = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
            putExtra(Intent.EXTRA_TITLE, "smolicon_$dateLabel.png")
        }
        startActivityForResult(intent, CREATE_FILE_REQ)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == CREATE_FILE_REQ && resultCode == RESULT_OK) {
            data?.data?.also { uri ->
                val descriptor = contentResolver.openFileDescriptor(uri, "w")
                FileOutputStream(descriptor!!.fileDescriptor).also { outputStream ->
                    val stream = ByteArrayOutputStream()
                    saveBitmap?.compress(Bitmap.CompressFormat.PNG, 100, stream)
                    outputStream.write(stream.toByteArray())
                    saveBitmap?.recycle()
                }
            }
        }
    }

    private fun restore(){
        val prefs = PreferenceManager.getDefaultSharedPreferences(this)
        when {
            prefs.contains("pixels") -> {
                val pixelStateStr = prefs.getString("pixels", "")?.dropLast(1)?.substring(1)
                pixelStateStr?.split(",").also { pixelState ->
                    repeat(256){ i ->
                        when (val stored = pixelState!![i].trim()) {
                            "null" -> screenMatrix.pixels[i] = null
                            else -> screenMatrix.pixels[i] = stored
                        }
                    }
                }
            }

        }

        val colour = prefs.getString("colour", "#00aa00")
        setColour(colour)
    }

    override fun onPause() {
        super.onPause()

        val prefs = PreferenceManager.getDefaultSharedPreferences(this)
        prefs.edit().putString("pixels", screenMatrix.pixels.contentToString()).apply()
        prefs.edit().putString("colour", screenMatrix.inkColour).apply()


    }
}