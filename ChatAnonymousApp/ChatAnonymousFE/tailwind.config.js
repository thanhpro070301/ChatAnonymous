/** @type {import('tailwindcss').Config} */
module.exports = {
  content: [
    "./index.html",
    "./src/**/*.{vue,js,ts,jsx,tsx}",
  ],
  darkMode: 'class',
  theme: {
    extend: {
      colors: {
        primary: {
          50: '#f0f9ff',
          100: '#e0f2fe',
          200: '#bae6fd',
          300: '#7dd3fc',
          400: '#38bdf8',
          500: '#0ea5e9',
          600: '#0284c7',
          700: '#0369a1',
          800: '#075985',
          900: '#0c4a6e',
          950: '#082f49',
        },
        secondary: {
          50: '#f5f3ff',
          100: '#ede9fe',
          200: '#ddd6fe',
          300: '#c4b5fd',
          400: '#a78bfa',
          500: '#8b5cf6',
          600: '#7c3aed',
          700: '#6d28d9',
          800: '#5b21b6',
          900: '#4c1d95',
          950: '#2e1065',
        },
        accent: {
          DEFAULT: '#ff9800',
          50: '#fff8eb',
          100: '#ffebc7',
          200: '#ffd488',
          300: '#ffb341',
          400: '#ff9010',
          500: '#ff7a04',
          600: '#d95c00',
          700: '#b44004',
          800: '#93320b',
          900: '#792c0c',
          950: '#461502',
        },
        error: {
          DEFAULT: '#f44336',
          50: '#fef3f2',
          100: '#fee5e2',
          200: '#fececa',
          300: '#feaba5',
          400: '#fa7b70',
          500: '#f44336',
          600: '#df2c20',
          700: '#bc2018',
          800: '#9a1c19',
          900: '#7f1d1a',
          950: '#450a08',
        },
        glass: {
          white: 'rgba(255, 255, 255, 0.7)',
          primary: 'rgba(76, 175, 80, 0.7)',
          dark: 'rgba(0, 0, 0, 0.4)',
        },
        neuro: {
          bg: '#e0e5ec',
          light: '#ffffff',
          dark: '#a3b1c6',
        },
      },
      fontFamily: {
        sans: ['Inter', 'Segoe UI', 'sans-serif'],
      },
      borderRadius: {
        'DEFAULT': '10px',
        'xl': '1rem',
        '2xl': '1.5rem',
        '3xl': '2rem',
      },
      boxShadow: {
        'DEFAULT': '0 2px 4px rgba(0, 0, 0, 0.1)',
        'md': '0 4px 6px rgba(0, 0, 0, 0.1)',
        'lg': '0 10px 15px rgba(0, 0, 0, 0.1)',
        'neuro': '5px 5px 10px #a3b1c6, -5px -5px 10px #ffffff',
        'neuro-inset': 'inset 5px 5px 10px #a3b1c6, inset -5px -5px 10px #ffffff',
        'neuro-flat': '3px 3px 6px #a3b1c6, -3px -3px 6px #ffffff',
        'glass': '0 8px 32px 0 rgba(31, 38, 135, 0.2)',
        'inner-light': 'inset 0 2px 4px 0 rgba(0, 0, 0, 0.06)',
      },
      backdropBlur: {
        'xs': '2px',
        'glass': '8px',
      },
      animation: {
        'bounce-light': 'bounce 2s infinite ease-in-out',
        'pulse-slow': 'pulse 3s infinite',
        'slide-up': 'slideUp 0.3s ease-out forwards',
        'fade-in': 'fadeIn 0.5s ease-out forwards',
        'slide-left': 'slideLeft 0.4s ease-out forwards',
        'slide-right': 'slideRight 0.4s ease-out forwards',
        'draw-path': 'drawPath 1.5s ease-in-out forwards',
        'orbit': 'orbit 3s linear infinite',
        'ping-slow': 'ping 3s cubic-bezier(0, 0, 0.2, 1) infinite',
        'media-appear': 'mediaAppear 0.5s ease-out forwards',
      },
      keyframes: {
        slideUp: {
          '0%': { opacity: 0, transform: 'translateY(10px)' },
          '100%': { opacity: 1, transform: 'translateY(0)' },
        },
        fadeIn: {
          '0%': { opacity: 0 },
          '100%': { opacity: 1 },
        },
        slideLeft: {
          '0%': { opacity: 0, transform: 'translateX(-20px)' },
          '100%': { opacity: 1, transform: 'translateX(0)' },
        },
        slideRight: {
          '0%': { opacity: 0, transform: 'translateX(20px)' },
          '100%': { opacity: 1, transform: 'translateX(0)' },
        },
        mediaAppear: {
          '0%': { opacity: 0, transform: 'translateY(8px) scale(0.98)' },
          '100%': { opacity: 1, transform: 'translateY(0) scale(1)' },
        },
        drawPath: {
          '0%': { strokeDashoffset: '1000' },
          '100%': { strokeDashoffset: '0' },
        },
        orbit: {
          '0%': { transform: 'translateX(-50%) translateY(-120%) rotate(0deg)' },
          '50%': { transform: 'translateX(150%) translateY(120%) rotate(180deg)' },
          '100%': { transform: 'translateX(-50%) translateY(-120%) rotate(360deg)' },
        },
      },
      transitionProperty: {
        'height': 'height',
        'spacing': 'margin, padding',
      },
    },
  },
  plugins: [],
} 