const webpack = require("webpack");

// Configurações do DevServer
config.devServer = {
    ...config.devServer,
    historyApiFallback: true,
    hot: true
};

// Configuração dos Polyfills
config.resolve = config.resolve || {};
config.resolve.fallback = {
    ...config.resolve.fallback,
    "os": require.resolve("os-browserify/browser"),
    "path": require.resolve("path-browserify"),
    "fs": false,
    "child_process": false,
    "crypto": false
};

// Plugins adicionais
config.plugins = config.plugins || [];
config.plugins.push(
    new webpack.ProvidePlugin({
        process: 'process/browser.js',
        Buffer: ['buffer', 'Buffer']
    })
);