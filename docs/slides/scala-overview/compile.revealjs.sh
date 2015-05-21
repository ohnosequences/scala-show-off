#!/bin/sh
pandoc -t revealjs --template=template.revealjs \
    --standalone --smart \
    --slide-level=2 \
    --variable theme="white" \
    --no-highlight \
    $1 -o ${1%.md}.html "${@:2}"
