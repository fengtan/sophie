# [Sophie](http://fengtan.github.io/sophie/)

[![Build Status](https://travis-ci.org/fengtan/sophie.svg?branch=master)](https://travis-ci.org/fengtan/sophie)

A [Solr](http://lucene.apache.org/solr/) browser and administration tool.

![Animation](https://raw.github.com/fengtan/sophie/master/anim.gif)

## Getting started

    git clone https://github.com/fengtan/sophie
    cd sophie
    mvn clean install -P linux # linux, macosx or win32
    java -jar target/sophie-*-with-dependencies.jar

Precompiled jar archives may also be downloaded from the [releases page](https://github.com/fengtan/sophie/releases).

## Resources

- [Issues](https://github.com/fengtan/sophie/issues)
- [Javadoc](http://fengtan.github.io/sophie/javadoc/)
- [Releases](https://github.com/fengtan/sophie/releases)
- [Website](http://fengtan.github.io/sophie/)
- [Wiki](https://github.com/fengtan/sophie/wiki)

## Similar tools

- [Solr Admin UI](https://cwiki.apache.org/confluence/display/solr/Overview+of+the+Solr+Admin+UI) is the default administration interface provided by Solr
- [Luke](https://github.com/DmitryKey/luke) is a GUI tool to inspect a Lucene index
- [Clue](https://github.com/javasoze/clue) is a CLI tool to inspect a Lucene index
- [Blacklight](https://github.com/projectblacklight/blacklight) discovers the structure of a Solr index and builds a web search interface accordingly

## License

GNU General Public License, Version 3.
