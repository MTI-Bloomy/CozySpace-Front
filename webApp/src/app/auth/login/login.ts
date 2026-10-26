import { Component, inject, signal } from '@angular/core';
import { FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';
import { RouterLink } from '@angular/router';
import { CustomButton } from '@ui/components/custom-button/custom-button';
import { CustomTextInput } from '@ui/components/custom-text-input/custom-text-input';
import { LoginRequestDto } from '../utils/auth.dto';
import { AuthHeader } from '../components/auth-header/auth-header';
import { AuthLayout } from '../components/auth-layout/auth-layout';

@Component({
  selector: 'app-login',
  standalone: true,
  imports: [ReactiveFormsModule, RouterLink, AuthLayout, AuthHeader, CustomTextInput, CustomButton],
  templateUrl: './login.html',
  styleUrl: './login.scss',
})
export class Login {
  private readonly fb = inject(FormBuilder).nonNullable;

  protected readonly form = this.fb.group({
    identifier: ['', [Validators.required]],
    password: ['', [Validators.required]],
  });

  protected readonly submitted = signal(false);

  protected showError(name: 'identifier' | 'password'): boolean {
    const control = this.form.controls[name];
    return control.invalid && (control.touched || this.submitted());
  }

  protected submit(): void {
    this.submitted.set(true);
    if (this.form.invalid) {
      return;
    }

    const { identifier, password } = this.form.getRawValue();
    const dto: LoginRequestDto = { email: identifier, password };

    // TODO: équivalent de stores.user.accept(UserStore.Intent.Login(dto))
    console.log('login', dto);
  }
}