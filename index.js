const { app: aplicativo, BrowserWindow: JanelaNavegador } = require('electron');
const caminho = require('path');

function criarJanela() {
  const janela = new JanelaNavegador({
    largura: 1024,
    altura: 768,
    webPreferences: {
      preload: caminho.join(__dirname, 'preload.js'),
      nodeIntegration: true,
      contextIsolation: false
    }
  });

  janela.loadFile('index.html');
}

aplicativo.whenReady().then(criarJanela);

aplicativo.on('window-all-closed', () => {
  if (process.platform !== 'darwin') {
    aplicativo.quit();
  }
});

aplicativo.on('activate', () => {
  if (JanelaNavegador.getAllWindows().length === 0) {
    criarJanela();
  }
});
