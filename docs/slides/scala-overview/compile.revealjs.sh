#!/bin/sh -e
pandoc -t revealjs --template=template.revealjs \
    --standalone --smart \
    --slide-level=2 \
    --variable theme="white" \
    --no-highlight \
    scala-overview.md -o scala-overview.html "${@}"
