#!/bin/bash

form=$1

parse_names() {
  first=$(echo $form | sed 's/&/\n/g' | sed 's/.*=//g' | head -1)
  last=$(echo $form | sed 's/&/\n/g' | sed 's/.*=//g' | tail -1)
  
  if [ $1 -eq 1 ]; then
    echo $first
  else
    echo $last
  fi
}

echo '<html>'
echo '<head>'
echo '<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">'
echo '<title>Hello World</title>'
echo '</head>'
echo '<body>'
echo "<h1>Hello person: $(echo $(parse_names 1)) $(echo $(parse_names 2))</h1>"
echo '</body>'
echo '</html>'

exit 0

