const EMAIL_REGEX = /^((?!\.)[\w\-_.]*[^.])(@\w+)(\.\w+(\.\w+)?[^.\W])$/;

export type PasswordStrength = 0 | 1 | 2 | 3 | 4;

export function isEmailValid(email: string): boolean {
  return email.trim() !== '' && EMAIL_REGEX.test(email);
}

export function isUsernameValid(username: string): boolean {
  return username.length > 1 && username.length < 20 && /^\p{L}+$/u.test(username);
  // TODO: ajuster la longueur max si besoin, éventuellement afficher des erreurs
}

export function getPasswordStrength(password: string): PasswordStrength {
  let score = 0;

  if (password.length >= 6) score++;
  if (/\p{Nd}/u.test(password)) score++;
  if (/\p{Lu}/u.test(password)) score++;
  if (/[^\p{L}\p{N}]/u.test(password)) score++;

  return score as PasswordStrength;
}

export function isPasswordValid(password: string): boolean {
  return getPasswordStrength(password) >= 4;
}

export function isPasswordConfirmationValid(password: string, confirmation: string): boolean {
  return confirmation !== '' && password === confirmation;
}