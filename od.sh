#!/usr/bin/env bash

echo ""
echo " .d8888b.  888b     d888  .d88888b.  888      8888888 .d8888b.   .d88888b.  888b    888"
echo "d88P  Y88b 8888b   d8888 d88P\" \"Y88b 888        888  d88P  Y88b d88P\" \"Y88b 8888b   888"
echo "Y88b.      88888b.d88888 888     888 888        888  888    888 888     888 88888b  888"
echo " \"Y888b.   888Y88888P888 888     888 888        888  888        888     888 888Y88b 888"
echo "    \"Y88b. 888 Y888P 888 888     888 888        888  888        888     888 888 Y88b888"
echo "      \"888 888  Y8P  888 888     888 888        888  888    888 888     888 888  Y88888"
echo "Y88b  d88P 888   \"   888 Y88b. .d88P 888        888  Y88b  d88P Y88b. .d88P 888   Y8888"
echo " \"Y8888P\"  888       888  \"Y88888P\"  88888888 8888888 \"Y8888P\"   \"Y88888P\"  888    Y888"
echo ""

dir=$(pwd)
echo "Directory: $dir"

./gradlew assembleRelease
gradleRetval=$?

if [ $gradleRetval != 0 ]; then
  echo "Gradle did not exit successfully: $gradleRetval"
  exit
else
  echo "Gradle build success"
fi

metadata=$(cat app/build/outputs/apk/release/output-metadata.json)
echo "$metadata" | jq '.'
echo "________________________________________________"

applicationId=$(echo "$metadata" | jq '.applicationId')
applicationId="${applicationId//\"}"

variantName="release"

versionName=$(echo "$metadata" | jq '.elements[0].versionName')
versionName="${versionName//\"}"

outputFile=$(echo "$metadata" | jq '.elements[0].outputFile')
outputFile="${outputFile//\"}"

echo "applicationId: $applicationId"
echo "variantName: $variantName"
echo "versionName: $versionName"
echo "outputFile: $outputFile"

filenameDirty="$applicationId.$variantName.$versionName"
filename="${filenameDirty//./_}.apk"

cp "app/build/outputs/apk/release/$outputFile" "$filename"

echo "________________________________________________"
echo "Output .apk: $filename"
checksum=$(md5 "$filename" | awk '{ print toupper($4) }')
echo "Checksum:"
printf "\t$checksum\n"
certSHA256A="B1:EB:49:C6:0C:C4:BC:BC:77:F6:47:EE:49:C5:5A:C1"
certSHA256B="2A:71:27:B8:87:85:52:94:9D:BB:45:71:BC:3C:67:EF"
echo "Signing cert SHA256:"
printf "\t$certSHA256A\n"
printf "\t$certSHA256B\n"
echo "________________________________________________"

echo "Generating Gemini .gmi"

rm download.gmi
touch download.gmi

echo "# Smolicon $versionName" >> download.gmi
echo "" >> download.gmi
echo "Download:" >> download.gmi
echo "=> $filename $filename" >> download.gmi
echo "" >> download.gmi
echo "Signing Cert SHA256:" >> download.gmi
echo "\`\`\`" >> download.gmi
echo "$certSHA256A" >> download.gmi
echo "$certSHA256B" >> download.gmi
echo "\`\`\`" >> download.gmi
echo "" >> download.gmi
echo ".apk file MD5 checksum:" >> download.gmi
echo "\`\`\`" >> download.gmi
echo "$checksum" >> download.gmi
echo "\`\`\`" >> download.gmi
echo "" >> download.gmi

echo "Uploading"

sftpCommand=$(sed '1q;d' ../_oppen_server.txt)
serverPath=$(sed '2q;d' ../_oppen_server.txt)

sftp "$sftpCommand" << SFTP
  pwd
  cd "$serverPath/software/smolicon/"
  put "$filename"
  put download.gmi
SFTP

rm "$filename"

echo "Finished"

exit 0