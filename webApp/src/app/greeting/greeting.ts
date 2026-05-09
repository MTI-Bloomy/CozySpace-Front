import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Greeting as KotlinGreeting } from 'shared';
import { JSLogo } from '../jslogo/jslogo';

@Component({
  selector: 'app-greeting',
  imports: [CommonModule, JSLogo],
  templateUrl: './greeting.html',
  styleUrl: './greeting.scss',
})
export class Greeting {
  isVisible = false;
  isAnimating = false;
  greeting = new KotlinGreeting();

  toggleVisibility() {
  if (this.isVisible && !this.isAnimating) {
    this.isAnimating = true;
    return;
  }

  this.isVisible = true;
  this.isAnimating = false;
}

  onAnimationEnd(event: AnimationEvent) {
    if (event.animationName === 'fadeOut') {
      this.isVisible = false;
      this.isAnimating = false;
    }
  }
}
