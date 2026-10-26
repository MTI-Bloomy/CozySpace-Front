import { computed, inject, Injectable, signal } from "@angular/core";
import { Router } from "@angular/router";
import { MessageService } from "primeng/api";

@Injectable({ providedIn: 'root' })
export class AuthService {
  private readonly router = inject(Router);
  private readonly toast = inject(MessageService);   // PrimeNG, pour remplacer la snackbar
  //private store!: UserStoreJs;

  private readonly state = signal({ loading: false, isLoggedIn: false });
  //readonly loading = computed(() => this.state().loading);
  readonly isLoggedIn = computed(() => this.state().isLoggedIn);

  /*async init(): Promise<void> {
    this.store = await createUserStore();

    this.store.subscribeState((s) =>
      this.state.set({ loading: s.loading, isLoggedIn: s.isLoggedIn }));

    this.store.subscribeLabels((l) => {
      switch (l.type) {
        case 'ERROR':
          this.toast.add({ severity: 'error', detail: l.message ?? undefined });
          break;
        case 'LOGIN_SUCCESS': this.router.navigateByUrl('/'); break;
        case 'LOGOUT': this.router.navigateByUrl('/login'); break;
      }
    });
  }

  login(email: string, password: string) { this.store.login(email, password); }
  register(email: string, password: string) { this.store.register(email, password); }
  logout() { this.store.logout(); }*/
}