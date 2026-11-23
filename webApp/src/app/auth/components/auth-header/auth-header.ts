import { Component, input } from '@angular/core';

@Component({
  selector: 'app-auth-header',
  standalone: true,
  templateUrl: './auth-header.html',
  styleUrl: './auth-header.scss',
})
export class AuthHeader {
  // Pas "title" : ce nom afficherait une infobulle native sur l'élément.
  heading = input.required<string>();
}