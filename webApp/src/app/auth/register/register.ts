import { Component, computed, inject } from '@angular/core';
import { toSignal } from '@angular/core/rxjs-interop';
import { FormBuilder, ReactiveFormsModule } from '@angular/forms';
import { RouterLink } from '@angular/router';
import { map } from 'rxjs';
import { CustomButton } from '@ui/components/custom-button/custom-button';
import { CustomTextInput } from '@ui/components/custom-text-input/custom-text-input';
import type { RegisterRequestDto } from '../utils/auth.dto';
import {
  isEmailValid,
  isPasswordConfirmationValid,
  isPasswordValid,
  isUsernameValid,
} from '../utils/auth.utils';
import { AuthHeader } from '../components/auth-header/auth-header';
import { AuthLayout } from '../components/auth-layout/auth-layout';
import { PasswordStrengthBar } from '../components/password-strength-bar/password-strength-bar';
import { AuthService } from '@core/services/auth.service';

@Component({
  selector: 'app-register',
  standalone: true,
  imports: [
    ReactiveFormsModule,
    RouterLink,
    AuthLayout,
    AuthHeader,
    CustomTextInput,
    CustomButton,
    PasswordStrengthBar,
  ],
  templateUrl: './register.html',
  styleUrl: './register.scss',
})
export class Register {
  private readonly fb = inject(FormBuilder).nonNullable;
  private readonly auth = inject(AuthService);

  protected readonly form = this.fb.group({
    username: [''],
    email: [''],
    password: [''],
    passwordConfirmation: [''],
  });

  // Les valeurs du formulaire sous forme de signal (équivalent des "remember { mutableStateOf }")
  protected readonly values = toSignal(
    this.form.valueChanges.pipe(map(() => this.form.getRawValue())),
    { initialValue: this.form.getRawValue() },
  );

  protected readonly usernameValid = computed(() => isUsernameValid(this.values().username));
  protected readonly emailValid = computed(() => isEmailValid(this.values().email));
  protected readonly passwordValid = computed(() => isPasswordValid(this.values().password));
  protected readonly confirmationValid = computed(() =>
    isPasswordConfirmationValid(this.values().password, this.values().passwordConfirmation),
  );

  protected readonly formValid = computed(
    () =>
      this.usernameValid() &&
      this.emailValid() &&
      this.passwordValid() &&
      this.confirmationValid(),
  );

  protected submit(): void {
    if (!this.formValid()) {
      return;
    }

    const { email, password } = this.form.getRawValue();
    const dto: RegisterRequestDto = { email, password };

    this.auth.register(dto.email, dto.password);
  }
}