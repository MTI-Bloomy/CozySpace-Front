import { Component, input } from '@angular/core';

@Component({
  selector: 'app-auth-layout',
  standalone: true,
  templateUrl: './auth-layout.html',
  styleUrl: './auth-layout.scss',
  host: {
    '[class.sign-up]': 'signUp()',
  },
})
export class AuthLayout {
  /** Page d'inscription : logo un peu plus petit, comme dans le Kotlin. */
  signUp = input<boolean>(false);
}