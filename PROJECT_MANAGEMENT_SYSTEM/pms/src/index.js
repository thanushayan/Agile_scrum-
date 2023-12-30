import React from 'react';
import ReactDOM from 'react-dom';
import { createRoot } from 'react-dom/client';
import App from './pageJSX/App';
import { DarkModeProvider } from './pageJSX/DarkModeProvider';
import { ToastContainer } from 'react-toastify';
import 'react-toastify/dist/ReactToastify.css';
import './css/index.css';
import reportWebVitals from './reportWebVitals';

const root = createRoot(document.getElementById('root'));

root.render(
  <div>
    <ToastContainer
      toastClassName={() =>
        'relative flex py-4 px-3 rounded overflow-hidden cursor-pointer bg-white shadow-lg'
      }
      bodyClassName={() => 'text-black text-base font-normal'}
      position="bottom-left"
      autoClose={4000}
      hideProgressBar={true}
      newestOnTop={false}
      closeButton={false}
      closeOnClick
      rtl={false}
      pauseOnFocusLoss
      draggable
      pauseOnHover
      theme="light"
    />
    <DarkModeProvider>
      <App />
    </DarkModeProvider>
  </div>
);

reportWebVitals();
