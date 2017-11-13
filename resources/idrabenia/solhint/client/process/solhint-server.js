const execSync = require('child_process').execSync;
const path = require('path');
const os = require('os');
const nodeBinPath = execSync('npm config get prefix').toString('utf-8').trim();
const http = require('http');
const url = require('url');
const fs = require('fs');

const configPath = path.join(process.cwd(), '.solhint.json');
const port = 3476;
var config = fs.existsSync(configPath) && readConfig();


function solhint() {
  if (os.platform().indexOf('win') === 0) {
    return require(path.join(nodeBinPath, 'node_modules', 'solhint', 'lib', 'index'));
  } else {
    return require(path.join(nodeBinPath, 'lib', 'node_modules', 'solhint', 'lib', 'index'));
  }
}

function watchConfig() {
  fs.exists(configPath, function (exists) {
    exists && fs.watchFile(configPath, readConfig);
  });
}

function readConfig() {
  const jsonValue = fs.readFileSync(configPath, 'utf-8');
  config = JSON.parse(jsonValue);
}

function requestHandler(request, response) {
  const filePath = url.parse(request.url, true).query.filePath;

  const errors = solhint().processFile(filePath, config);
  response.end(JSON.stringify(errors.messages));
}

function init() {
  watchConfig();

  http
    .createServer(requestHandler)
    .listen(port, function (err) {
      if (err) {
        return console.log('Error happened', err);
      }

      console.log('Server is listening on ' + port);
    });
}

init();

