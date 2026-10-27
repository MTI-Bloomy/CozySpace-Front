import { inject, type ApplicationConfig, provideAppInitializer, provideBrowserGlobalErrorListeners } from '@angular/core';
import { provideRouter } from '@angular/router';
import { MessageService } from 'primeng/api';
import { providePrimeNG } from 'primeng/config';

import { routes } from './app.routes';
import { AuthService } from './core/services/auth.service';
import { CozyPreset } from './core/theme/cozy-preset';

export const appConfig: ApplicationConfig = {
  providers: [
    provideBrowserGlobalErrorListeners(),
    provideRouter(routes),
    MessageService,
    provideAppInitializer(() => inject(AuthService).init()),
    providePrimeNG({
      theme: {
        preset: CozyPreset,
        options: { darkModeSelector: false }
      }
    })
  ]
};
