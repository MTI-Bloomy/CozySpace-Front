import { computed, inject, Injectable, signal } from '@angular/core';
import { Router } from "@angular/router";
import { MessageService } from "primeng/api";
import { createUserStore, type UserStoreJs } from 'shared';

@Injectable({ providedIn: 'root' })
export class AuthService {
  private readonly router = inject(Router);
  private readonly toast = inject(MessageService);
  private store: UserStoreJs | null = null;

  private readonly state = signal({ loading: true, isLoggedIn: false });
  readonly loading = computed(() => this.state().loading);
  readonly isLoggedIn = computed(() => this.state().isLoggedIn);

  async init(): Promise<void> {
    const store = await createUserStore();
    this.store = store;

    store.subscribeState((state) =>
      this.state.set({ loading: state.loading, isLoggedIn: state.isLoggedIn }),
    );

    store.subscribeLabels((label) => {
      switch (label.type) {
        case 'ERROR':
          this.toast.add({ severity: 'error', detail: label.message ?? undefined });
          break;
        case 'LOGIN_SUCCESS':
          void this.router.navigateByUrl('/');
          break;
        case 'LOGOUT':
          void this.router.navigateByUrl('/login');
          break;
      }
    });
  }

  login(email: string, password: string): void {
    this.requireStore().login(email, password);
  }

  register(email: string, password: string): void {
    this.requireStore().register(email, password);
  }

  logout(): void {
    this.requireStore().logout();
  }

  private requireStore(): UserStoreJs {
    if (this.store === null) {
      throw new Error('AuthService has not been initialized');
    }

    return this.store;
  }
}