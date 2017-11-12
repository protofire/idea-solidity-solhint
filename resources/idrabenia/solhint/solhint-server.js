const { execSync } = require('child_process');
const nodeBinPath = execSync('which solhint').toString('utf-8').replace('solhint', '').replace('\n', '');
const solhint = require(`${nodeBinPath}../lib/node_modules/solhint/lib/index`);
const http = require('http');
const port = 3476;


const requestHandler = (request, response) => {
  const filePath = request.url;

  const errors = solhint.processFile(filePath, {});
  response.end(JSON.stringify(errors.messages));
};

function init() {
  http
    .createServer(requestHandler)
    .listen(port, (err) => {
      if (err) {
        return console.log('Error happened', err);
      }

      console.log(`Server is listening on ${port}`);
    });
}

init();

