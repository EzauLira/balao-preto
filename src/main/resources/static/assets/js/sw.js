const CACHE_NAME = 'balao-preto-pwa-cache-v1';
const URLS_TO_CACHE = [
  '/',
  '/index.html',
  '/bemVindo.html',
  '/login.html',
  '/menu.html',
  '/cadastro.html',
  '/confirmacaoEmail.html',
  '/assets/css/bemVindo.css',
  '/assets/css/login.css',
  '/assets/css/menu.css',
  '/assets/js/bemVindo.js',
  '/assets/js/login.js',
  '/assets/js/menu.js',
  '/assets/icons/icon-192.png',
  '/assets/icons/icon-512.png',
  '/manifest.json'
];

// Instalar o Service Worker e armazenar recursos
self.addEventListener('install', event => {
  event.waitUntil(
    caches.open(CACHE_NAME)
      .then(cache => {
        return cache.addAll(URLS_TO_CACHE);
      })
  );
});

// Ativar e limpar caches antigos
self.addEventListener('activate', event => {
  const cacheWhitelist = [CACHE_NAME];
  event.waitUntil(
    caches.keys().then(keyList => {
      return Promise.all(
        keyList.map(key => {
          if (!cacheWhitelist.includes(key)) {
            return caches.delete(key);
          }
        })
      );
    })
  );
});

// Intercepta requisições e responde com cache ou rede
self.addEventListener('fetch', event => {
  event.respondWith(
    caches.match(event.request)
      .then(response => {
        return response || fetch(event.request);
      })
  );
});