import { defineConfig } from 'vite'
import react from '@vitejs/plugin-react'

export default defineConfig({
  plugins: [react()],
  server: {
    proxy: {
      '/api/tasks/create': {
        target: 'http://localhost:8080',
        changeOrigin: true,
        rewrite: (path) => path.replace('/api/tasks/create', '/api/letter_tasks/create/letterTask')
      },
      '/api': {
        target: 'http://localhost:8080',
        changeOrigin: true
      },
      '/rsa-service': {
        target: 'http://localhost:8080',
        changeOrigin: true
      }
    }
  }
})
