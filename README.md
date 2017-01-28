# [Sophie](http://fengtan.github.io/sophie/)

[![Build Status](https://travis-ci.org/fengtan/sophie.svg?branch=master)](https://travis-ci.org/fengtan/sophie)

A [Solr](http://lucene.apache.org/solr/) browser and administration tool.

![Animation](https://raw.github.com/fengtan/sophie/master/anim.gif)

## Getting started

Either download a precompiled jar archive from the [releases page](https://github.com/fengtan/sophie/releases) and run it:

    java -jar sophie-1.2.1-linux-with-dependencies.jar # Linux, Windows
    java -XstartOnFirstThread -jar sophie-1.2.1-linux-with-dependencies.jar # Mac OS

Or install the .deb package if you are on Debian/Ubuntu:

    sudo dpkg -i sophie_1.2.1_all.deb
    sophie &

Or compile the sources yourself and run the script:

    git clone https://github.com/fengtan/sophie
    cd sophie
    mvn clean install -P linux # linux, macosx or win32
    ./sophie-start.sh

More details can be found on the [wiki](https://github.com/fengtan/sophie/wiki/).

## Resources

- [Issues](https://github.com/fengtan/sophie/issues)
- [Javadoc](http://fengtan.github.io/sophie/javadoc/)
- [Releases](https://github.com/fengtan/sophie/releases) ([Feed](https://github.com/fengtan/sophie/releases.atom))
- [Website](http://fengtan.github.io/sophie/)
- [Wiki](https://github.com/fengtan/sophie/wiki)

## Similar tools

- [Solr Admin UI](https://cwiki.apache.org/confluence/display/solr/Overview+of+the+Solr+Admin+UI) is the administration interface embedded in Solr
- [Luke](https://github.com/DmitryKey/luke) is a GUI tool to inspect a Lucene index
- [Clue](https://github.com/javasoze/clue) is a CLI tool to inspect a Lucene index
- [Blacklight](https://github.com/projectblacklight/blacklight) discovers the structure of a Solr index and builds a web search interface accordingly
- [Solr Explorer](https://github.com/cominvent/solr-explorer) acts as a proxy and offers a web interface to explore Solr
- [Ajax Solr](https://github.com/evolvingweb/ajax-solr) is a Javascript framework for creating user interfaces to Solr
- [Marple](https://github.com/flaxsearch/marple) offers a web interface and a REST API to browse a Lucene index

## License

GNU General Public License, Version 3.
