# [Sophie](http://fengtan.github.io/sophie/)

[![Build Status](https://travis-ci.org/fengtan/sophie.svg?branch=master)](https://travis-ci.org/fengtan/sophie)

A [Solr](http://lucene.apache.org/solr/) browser and administration tool.

![Animation](https://raw.github.com/fengtan/sophie/master/anim.gif)

## What is included

- Document browser: list, filter and sort documents stored in a Solr index and inspect their contents
- Operations on documents: create, update, delete, clone Solr documents
- Operations on the index: clear, commit and optimize the index
- Administration tools: export index into a CSV file, backup and restore the index
- Field browser: list all fields configued on Solr and inspect their attributes
- Core browser: list all cores hosted on a Solr instance and inspect their attributes
- Operations on cores: create, delete, rename, swap, reload cores
- System browser: view all system properties
- Config files browser: list all Solr config files and view their contents

## Getting started

Either download and run a [jar archive](https://github.com/fengtan/sophie/releases):

    java -jar sophie-1.2.1-*-with-dependencies.jar
    # Mac OS requires the -XstartOnFirstThread option to be set:
    java -XstartOnFirstThread -jar sophie-1.2.1-macosx-with-dependencies.jar

Or download and install a [.deb package](https://github.com/fengtan/sophie/releases) if you are on Debian/Ubuntu:

    sudo dpkg -i sophie_1.2.1_all.deb
    sophie &

Or compile from the sources:

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

## Thanks

This project is powered by [SolrJ](https://cwiki.apache.org/confluence/display/solr/Using+SolrJ) and [SWT](https://www.eclipse.org/swt/).
