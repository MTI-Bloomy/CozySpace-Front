import { definePreset } from '@primeuix/themes';
import Aura from '@primeuix/themes/aura';

export const CozyPreset = definePreset(Aura, {
  semantic: {
    primary: {
      400: '#60a17a',
      500: '#549772',
      600: '#3b8362',
      700: '#2e785a',
    },
  },
});