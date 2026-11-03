import { Component, computed, input } from '@angular/core';
import { getPasswordStrength } from '../../utils/auth.utils';

const COLORS = [
  'var(--strength-neutral)',
  'var(--strength-red)',
  'var(--strength-orange)',
  'var(--green-mid-light)',
  'var(--green-dark)',
];

const LABELS = ['Password strength', 'Very weak', 'Weak', 'Medium', 'Strong'];

@Component({
  selector: 'app-password-strength-bar',
  standalone: true,
  templateUrl: './password-strength-bar.html',
  styleUrl: './password-strength-bar.scss',
})
export class PasswordStrengthBar {
  password = input<string>('');

  protected readonly segments = [1, 2, 3, 4];
  protected readonly strength = computed(() => getPasswordStrength(this.password()));
  protected readonly color = computed(() => COLORS[this.strength()]);
  protected readonly label = computed(() => LABELS[this.strength()]);
}