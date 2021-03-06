import {APP_BASE_HREF} from '@angular/common';

import {ngExpressEngine} from '@nguniversal/express-engine';
import * as express from 'express';
import {existsSync} from 'fs';
import {createProxyMiddleware} from 'http-proxy-middleware';
import {join} from 'path';
import 'zone.js/node';
import {API_BASE_URL} from './src/app/app.tokens';
import {environment} from './src/environments/environment';

import {AppServerModule} from './src/main.server';

// The Express app is exported so that it can be used by serverless Functions.
export function app(): express.Express {
  const server = express();
  const standardDistFolder = join(process.cwd(), 'dist/WebGUI/browser');
  const distFolder = existsSync(standardDistFolder) ? standardDistFolder : join(process.cwd(), 'browser');
  const indexHtml = existsSync(join(distFolder, 'index.original.html')) ? 'index.original.html' : 'index';

  const apiAddress = process.env.API_ADDRESS || 'http://192.168.1.84:8080';

  // Our Universal express-engine (found @ https://github.com/angular/universal/tree/master/modules/express-engine)
  server.engine('html', ngExpressEngine({
    bootstrap: AppServerModule,
    providers: [{ provide: API_BASE_URL, useValue: `${apiAddress}${environment.apiPath}` }]
  }));
  server.set('view engine', 'html');

  server.set('views', distFolder);
  server.use(environment.apiPath, createProxyMiddleware({target: apiAddress, changeOrigin: true}));

  // Serve static files from /browser
  server.get('*.*', express.static(distFolder, {
    maxAge: '1y'
  }));

  // All regular routes use the Universal engine
  server.get('*', (req, res) => {
    res.render(indexHtml, {req, providers: [{provide: APP_BASE_HREF, useValue: req.baseUrl}]});
  });

  return server;
}

function run(): void {
  const port = process.env.PORT || 4000;

  // Start up the Node server
  const server = app();
  server.listen(port, () => {
    console.log(`Node Express server listening on http://localhost:${port}`);
  });
}

// Webpack will replace 'require' with '__webpack_require__'
// '__non_webpack_require__' is a proxy to Node 'require'
// The below code is to ensure that the server is run only when not requiring the bundle.
declare const __non_webpack_require__: NodeRequire;
const mainModule = __non_webpack_require__.main;
const moduleFilename = mainModule && mainModule.filename || '';
if (moduleFilename === __filename || moduleFilename.includes('iisnode')) {
  run();
}

export * from './src/main.server';
