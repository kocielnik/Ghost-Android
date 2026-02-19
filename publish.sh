#!/usr/bin/env bash

curl -L \
  -X POST \
  -H "Accept: application/vnd.github+json" \
  -H 'authorization: Bearer ${{ secrets.GITHUB_TOKEN }}' \
  -H "X-GitHub-Api-Version: 2022-11-28" \
  https://api.github.com/repos/kocielnik/Ghost-Android/releases \
  -d "{'tag_name':'$VERSION','target_commitish':'ci','name':'$VERSION','body':'Ghost Android v. $VERSION','draft':true,'prerelease':false,'generate_release_notes':false}"
