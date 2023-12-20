#!/usr/bin/env bash

echo ""
echo "######  ####### ######  #######  #####  #       #######    #    #     # "
echo "#     # #       #     # #     # #     # #       #         # #   ##    # "
echo "#     # #       #     # #     # #       #       #        #   #  # #   # "
echo "######  #####   ######  #     # #       #       #####   #     # #  #  # "
echo "#   #   #       #       #     # #       #       #       ####### #   # # "
echo "#    #  #       #       #     # #     # #       #       #     # #    ## "
echo "#     # ####### #       #######  #####  ####### ####### #     # #     # "
echo ""

branch=$(git branch --show-current)
gitremote=$(git remote -v | grep '(push)')

name=$(echo $gitremote | awk '{print $1}')
remote=$(echo $gitremote | awk '{print $2}')

echo "Remote: $remote"

rm -rf .git
git init
git checkout -b $branch
git add .
git commit -m 'börja'

git remote add $name $remote
git push -u --force $name $branch

echo "Complete"