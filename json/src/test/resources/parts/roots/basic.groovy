package parts.roots

import cd.go.contrib.plugins.configrepo.groovy.dsl.GoCD

GoCD.script {
  environments {
    environment('first') {
    }
    environment('second') {
    }
  }
  pipelines {
    pipeline('pipe1') {
      group = 'foo'
      params = [
        BROWSER: 'firefox',
        OS     : 'linux',
      ]
    }
    pipeline('pipe2') {
      group = 'foo'
    }
  }
}
