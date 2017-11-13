const execSync = require('child_process').execSync;
const path = require('path');
const os = require('os');
const nodeBinPath = execSync('npm config get prefix').toString('utf-8').trim();
const http = require('http');
const url = require('url');
const port = 3476;


function solhint() {
  if (os.platform().indexOf('win') === 0) {
    return require(path.join(nodeBinPath, 'node_modules', 'solhint', 'lib', 'index'));
  } else {
    return require(path.join(nodeBinPath, 'lib', 'node_modules', 'solhint', 'lib', 'index'));
  }
}

function requestHandler(request, response) {
  const filePath = url.parse(request.url, true).query.filePath;

  const errors = solhint().processFile(filePath, {});
  response.end(JSON.stringify(errors.messages));
};

function init() {
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

