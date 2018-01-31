#!/usr/bin/env python
# -*- coding: utf-8 -*-

import urllib2
import sys

after_url = sys.argv[1]
print after_url

my_url = 'http://localhost:1314/spring-boot-template/test/' + after_url
urllib2.urlopen(url=my_url)
