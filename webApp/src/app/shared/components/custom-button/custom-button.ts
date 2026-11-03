import { Component, input } from '@angular/core';

export type CustomButtonVariant = 'primary' | 'secondary' | 'text';

@Component({
  selector: 'app-custom-button',
  standalone: true,
  templateUrl: './custom-button.html',
  styleUrl: './custom-button.scss',
  host: {
    '[class.fluid]': 'fluid()',
  },
})
export class CustomButton {
  label = input<string>();
  type = input<'button' | 'submit' | 'reset'>('button');
  variant = input<CustomButtonVariant>('primary');
  disabled = input<boolean>(false);
  loading = input<boolean>(false);
  fluid = input<boolean>(false);
  ariaLabel = input<string>();

  /** Classe primeicons, ex: 'pi-check', 'pi-plus' */
  icon = input<string>();
}