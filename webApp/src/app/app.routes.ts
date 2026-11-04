import type { Routes } from '@angular/router';
import { Login } from './auth/login/login';
import { Register } from './auth/register/register';
import { Home } from './home/home/home';
import { authGuard, guestGuard } from './core/guards/auth.guard';

export const routes: Routes = [
    { path: 'login', component: Login, canActivate: [guestGuard] },
    { path: 'register', component: Register, canActivate: [guestGuard] },
    { path: '', component: Home, canActivate: [authGuard] },
    { path: '**', redirectTo: '' },
];
