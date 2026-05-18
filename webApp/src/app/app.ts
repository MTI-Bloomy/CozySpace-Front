import type { AfterViewInit} from '@angular/core';
import { Component, signal } from '@angular/core';
import { RouterOutlet } from '@angular/router';

@Component({
  selector: 'app-root',
  imports: [RouterOutlet],
  templateUrl: './app.html',
  styleUrl: './app.scss'
})
export class App implements AfterViewInit {
  protected readonly title = signal('webApp');
  protected readonly isReady = signal(false);

  ngAfterViewInit(): void {
    setTimeout(() => this.isReady.set(true));
  }
}
