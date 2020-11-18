#!/bin/bash

rand() {
  head /dev/urandom | tr -dc A-Za-z0-9 | head -c 13 ; echo ''
}

echo '<html>'
echo '<head>'
echo '<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">'
echo '<title>Hello World</title>'
echo '</head>'
echo '<body>'
echo '<h1>Randomly generated strings</h1>'
echo '<ol>'
echo "<li>$(echo $(rand))</li>"
echo "<li>$(echo $(rand))</li>"
echo "<li>$(echo $(rand))</li>"
echo "<li>$(echo $(rand))</li>"
echo "<li>$(echo $(rand))</li>"
echo '</ol>'
echo '</body>'
echo '</html>'

exit 0

