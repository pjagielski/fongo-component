#!/bin/bash
COVERALLS_URL='https://coveralls.io/api/v1/jobs'
lein cloverage -o target/coveralls --coveralls
curl -F 'json_file=@target/coveralls/coveralls.json' "$COVERALLS_URL"
