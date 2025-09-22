import { defineConfig } from 'vite'
import react from '@vitejs/plugin-react'

// https://vite.dev/config/
export default defineConfig({
  base: '/',
  build: {
    outDir: '.',
    emptyOutDir: false // Prevent deleting everything in root
  },
  plugins: [react()],
})
