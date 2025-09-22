# FSAMarsWorld Frontend (Vite + React)

This is the frontend for FSAMarsWorld, built with React and Vite for fast development and modern tooling.

## Getting Started

1. **Install dependencies:**
   ```sh
   npm install
   ```
2. **Run the development server:**
   ```sh
   npm run dev
   ```
   The app will start on http://localhost:5173 by default.

## Project Structure

- `src/` - Main source code
  - `App.jsx` - Main React component
  - `main.jsx` - Entry point
  - `assets/` - Static assets
- `public/` - Public files
- `vite.config.js` - Vite configuration

## Connecting to Backend

- Update API calls in your React components to point to your Spring Boot backend (default: http://localhost:8080).
- Use environment variables or configuration files for API URLs if needed.

## Building for Production

```sh
npm run build
```

## Learn More
- [Vite Documentation](https://vitejs.dev/)
- [React Documentation](https://react.dev/)

## License
See main project LICENSE.
