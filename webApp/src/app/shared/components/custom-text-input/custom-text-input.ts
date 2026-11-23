import { Component, computed, forwardRef, input, signal } from '@angular/core';
import { type ControlValueAccessor, NG_VALUE_ACCESSOR } from '@angular/forms';

export type CustomInputType = 'text' | 'email' | 'password' | 'tel' | 'url' | 'search';

let nextId = 0;

@Component({
  selector: 'app-custom-text-input',
  standalone: true,
  templateUrl: './custom-text-input.html',
  styleUrl: './custom-text-input.scss',
  providers: [
    {
      provide: NG_VALUE_ACCESSOR,
      useExisting: forwardRef(() => CustomTextInput),
      multi: true,
    },
  ],
})
export class CustomTextInput implements ControlValueAccessor {
  // --- Inputs (tous optionnels) ---
  label = input<string>();
  placeholder = input<string>('');
  ariaLabel = input<string>();
  small = input<string>();
  error = input<boolean>(false);
  type = input<CustomInputType>('text');
  required = input<boolean>(false);
  autocomplete = input<string>();

  /** Classe primeicons, ex: 'pi-user', 'pi-lock', 'pi-envelope' */
  icon = input<string>();

  /** Champ valide : l'icône devient une coche verte (comme dans le formulaire d'inscription) */
  success = input<boolean>(false);

  /** Mot de passe : affiche le bouton œil pour montrer/masquer */
  toggleMask = input<boolean>(true);

  // --- État interne ---
  protected readonly inputId = `custom-input-${nextId++}`;
  protected readonly smallId = `${this.inputId}-small`;
  protected readonly value = signal<string>('');
  protected readonly disabled = signal<boolean>(false);
  protected readonly visible = signal<boolean>(false);

  protected readonly effectiveType = computed(() =>
    this.type() === 'password' && this.visible() ? 'text' : this.type(),
  );

  // --- ControlValueAccessor ---
  private onChange: (value: string) => void = () => { /* empty */ };
  protected onTouched: () => void = () => { /* empty */ };

  writeValue(value: string | null): void {
    this.value.set(value ?? '');
  }

  registerOnChange(fn: (value: string) => void): void {
    this.onChange = fn;
  }

  registerOnTouched(fn: () => void): void {
    this.onTouched = fn;
  }

  setDisabledState(isDisabled: boolean): void {
    this.disabled.set(isDisabled);
  }

  protected onInput(event: Event): void {
    const value = (event.target as HTMLInputElement).value;
    this.value.set(value);
    this.onChange(value);
  }

  protected toggleVisibility(): void {
    this.visible.update((v) => !v);
  }
}