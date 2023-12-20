package oppen.smolicon.view

import android.content.res.Resources
import android.graphics.Canvas
import oppen.smolicon.lib.Skiss
import oppen.smolicon.lib.SkissView
import oppen.smolicon.lib.color
import kotlin.math.floor

/**
 *
 * See: https://material.io/resources/color/
 *
 */
class ColourMatrix(
    view: SkissView,
    private val background: Int,
    private val listener: Listener?): Skiss(view) {

    private val screenWidth = Resources.getSystem().displayMetrics.widthPixels
    private var cellDiam = 10
    private val pixels = arrayOfNulls<String>(191)

    override fun setup(width: Int, height: Int) {
        cellDiam = screenWidth/10
        skissView.layoutParams.height = cellDiam * 19

        populatePixels()
    }

    override fun update(canvas: Canvas) {
        background(background)

        repeat(19){ yIndex ->
            repeat(10){ xIndex ->
                val colour = pixels[(yIndex * 10) + xIndex] ?: "#dddddd"
                fill(color(colour))
                square((xIndex * cellDiam) + 1,(yIndex * cellDiam) + 1, cellDiam - 2)
            }
        }
    }

    private fun populatePixels(){

        //Red
        pixels[0] = "#ffebee"
        pixels[1] = "#ffcdd2"
        pixels[2] = "#ef9a9a"
        pixels[3] = "#e57373"
        pixels[4] = "#ef5350"
        pixels[5] = "#f44336"
        pixels[6] = "#e53935"
        pixels[7] = "#d32f2f"
        pixels[8] = "#c62828"
        pixels[9] = "#b71c1c"

        //Pink
        pixels[10] = "#fce4ec"
        pixels[11] = "#f8bbd0"
        pixels[12] = "#f48fb1"
        pixels[13] = "#f06292"
        pixels[14] = "#ec407a"
        pixels[15] = "#e91e63"
        pixels[16] = "#d81b60"
        pixels[17] = "#c2185b"
        pixels[18] = "#ad1457"
        pixels[19] = "#880e4f"

        //Purple
        pixels[20] = "#f3e5f5"
        pixels[21] = "#e1bee7"
        pixels[22] = "#ce93d8"
        pixels[23] = "#ba68c8"
        pixels[24] = "#ab47bc"
        pixels[25] = "#9c27b0"
        pixels[26] = "#8e24aa"
        pixels[27] = "#7b1fa2"
        pixels[28] = "#6a1b9a"
        pixels[29] = "#4a148c"

        //Deep Purple
        pixels[30] = "#ede7f6"
        pixels[31] = "#d1c4e9"
        pixels[32] = "#b39ddb"
        pixels[33] = "#9575cd"
        pixels[34] = "#7e57c2"
        pixels[35] = "#673ab7"
        pixels[36] = "#5e35b1"
        pixels[37] = "#512da8"
        pixels[38] = "#4527a0"
        pixels[39] = "#311b92"

        //Indigo
        pixels[40] = "#e8eaf6"
        pixels[41] = "#c5cae9"
        pixels[42] = "#9fa8da"
        pixels[43] = "#7986cb"
        pixels[44] = "#5c6bc0"
        pixels[45] = "#3f51b5"
        pixels[46] = "#3949ab"
        pixels[47] = "#303f9f"
        pixels[48] = "#283593"
        pixels[49] = "#1a237e"

        //Blue
        pixels[50] = "#e3f2fd"
        pixels[51] = "#bbdefb"
        pixels[52] = "#90caf9"
        pixels[53] = "#64b5f6"
        pixels[54] = "#42a5f5"
        pixels[55] = "#2196f3"
        pixels[56] = "#1e88e5"
        pixels[57] = "#1976d2"
        pixels[58] = "#1565c0"
        pixels[59] = "#0d47a1"

        //Light Blue
        pixels[60] = "#e1f5fe"
        pixels[61] = "#b3e5fc"
        pixels[62] = "#81d4fa"
        pixels[63] = "#4fc3f7"
        pixels[64] = "#29b6f6"
        pixels[65] = "#03a9f4"
        pixels[66] = "#039be5"
        pixels[67] = "#0288d1"
        pixels[68] = "#0277bd"
        pixels[69] = "#01579b"

        //Cyan
        pixels[70] = "#e0f7fa"
        pixels[71] = "#b2ebf2"
        pixels[72] = "#80deea"
        pixels[73] = "#4dd0e1"
        pixels[74] = "#26c6da"
        pixels[75] = "#00bcd4"
        pixels[76] = "#00acc1"
        pixels[77] = "#0097a7"
        pixels[78] = "#00838f"
        pixels[79] = "#006064"

        //Teal
        pixels[80] = "#e0f2f1"
        pixels[81] = "#b2dfdb"
        pixels[82] = "#80cbc4"
        pixels[83] = "#4db6ac"
        pixels[84] = "#26a69a"
        pixels[85] = "#009688"
        pixels[86] = "#00897b"
        pixels[87] = "#00796b"
        pixels[88] = "#00695c"
        pixels[89] = "#004d40"

        //Green
        pixels[90] = "#e8f5e9"
        pixels[91] = "#c8e6c9"
        pixels[92] = "#a5d6a7"
        pixels[93] = "#81c784"
        pixels[94] = "#66bb6a"
        pixels[95] = "#4caf50"
        pixels[96] = "#43a047"
        pixels[97] = "#388e3c"
        pixels[98] = "#2e7d32"
        pixels[99] = "#1b5e20"

        //Light Green
        pixels[100] = "#f1f8e9"
        pixels[101] = "#dcedc8"
        pixels[102] = "#c5e1a5"
        pixels[103] = "#aed581"
        pixels[104] = "#9ccc65"
        pixels[105] = "#8bc34a"
        pixels[106] = "#7cb342"
        pixels[107] = "#689f38"
        pixels[108] = "#558b2f"
        pixels[109] = "#33691e"

        //Lime
        pixels[110] = "#f9fbe7"
        pixels[111] = "#f0f4c3"
        pixels[112] = "#e6ee9c"
        pixels[113] = "#dce775"
        pixels[114] = "#d4e157"
        pixels[115] = "#cddc39"
        pixels[116] = "#c0ca33"
        pixels[117] = "#afb42b"
        pixels[118] = "#9e9d24"
        pixels[119] = "#827717"

        //Yellow
        pixels[120] = "#fffde7"
        pixels[121] = "#fff9c4"
        pixels[122] = "#fff59d"
        pixels[123] = "#fff176"
        pixels[124] = "#ffee58"
        pixels[125] = "#ffeb3b"
        pixels[126] = "#fdd835"
        pixels[127] = "#fbc02d"
        pixels[128] = "#f9a825"
        pixels[129] = "#f57f17"

        //Amber
        pixels[130] = "#fff8e1"
        pixels[131] = "#ffecb3"
        pixels[132] = "#ffe082"
        pixels[133] = "#ffd54f"
        pixels[134] = "#ffca28"
        pixels[135] = "#ffc107"
        pixels[136] = "#ffb300"
        pixels[137] = "#ffa000"
        pixels[138] = "#ff8f00"
        pixels[139] = "#ff6f00"

        //Orange
        pixels[140] = "#fff3e0"
        pixels[141] = "#ffe0b2"
        pixels[142] = "#ffcc80"
        pixels[143] = "#ffb74d"
        pixels[144] = "#ffa726"
        pixels[145] = "#ff9800"
        pixels[146] = "#fb8c00"
        pixels[147] = "#f57c00"
        pixels[148] = "#ef6c00"
        pixels[149] = "#e65100"

        //Deep Orange
        pixels[150] = "#fbe9e7"
        pixels[151] = "#ffccbc"
        pixels[152] = "#ffab91"
        pixels[153] = "#ff8a65"
        pixels[154] = "#ff7043"
        pixels[155] = "#ff5722"
        pixels[156] = "#f4511e"
        pixels[157] = "#e64a19"
        pixels[158] = "#d84315"
        pixels[159] = "#bf360c"

        //Brown
        pixels[160] = "#efebe9"
        pixels[161] = "#d7ccc8"
        pixels[162] = "#bcaaa4"
        pixels[163] = "#a1887f"
        pixels[164] = "#8d6e63"
        pixels[165] = "#795548"
        pixels[166] = "#6d4c41"
        pixels[167] = "#5d4037"
        pixels[168] = "#4e342e"
        pixels[169] = "#3e2723"

        //Grey
        pixels[170] = "#fafafa"
        pixels[171] = "#f5f5f5"
        pixels[172] = "#eeeeee"
        pixels[173] = "#e0e0e0"
        pixels[174] = "#bdbdbd"
        pixels[175] = "#9e9e9e"
        pixels[176] = "#757575"
        pixels[177] = "#616161"
        pixels[178] = "#424242"
        pixels[179] = "#212121"

        //Blue Grey
        pixels[180] = "#eceff1"
        pixels[181] = "#cfd8dc"
        pixels[182] = "#b0bec5"
        pixels[183] = "#90a4ae"
        pixels[184] = "#78909c"
        pixels[185] = "#607d8b"
        pixels[186] = "#546e7a"
        pixels[187] = "#455a64"
        pixels[188] = "#37474f"
        pixels[189] = "#263238"
    }

    override fun onTouch(x: Int, y: Int) {
        super.onTouch(x, y)

        val xx = (x - (x % cellDiam)) / cellDiam
        val yy = (y - (y % cellDiam)) / cellDiam
        val index = floor((yy * 10.0) + xx).toInt()
        when {
            index < 256 -> listener?.onColour("${pixels[index]}")
            else -> println("Smolicon: Index out of bounds: $index")
        }
    }

    interface Listener{
        fun onColour(colour: String)
    }
}