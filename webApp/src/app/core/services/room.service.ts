import { computed, inject, Injectable, signal } from '@angular/core';
import { MessageService } from 'primeng/api';
import { createRoomStore, type RoomStateJs, type RoomStoreJs } from 'shared';

@Injectable({ providedIn: 'root' })
export class RoomService {
  private readonly toast = inject(MessageService);
  private store: RoomStoreJs | null = null;
  private initPromise: Promise<void> | null = null;

  private readonly state = signal<RoomStateJs | null>(null);
  readonly loading = computed(() => this.state()?.loading ?? false);
  readonly rooms = computed(() => this.state()?.rooms ?? []);
  readonly error = computed(() => this.state()?.error ?? null);

  /** Idempotent : peut être appelé plusieurs fois, le store n'est créé qu'une seule fois. */
  init(): Promise<void> {
    return (this.initPromise ??= this.doInit());
  }

  async getRooms(houseId: string, saveMode = false): Promise<void> {
    (await this.ready()).getRooms(houseId, saveMode);
  }

  async clear(): Promise<void> {
    (await this.ready()).clear();
  }

  private async doInit(): Promise<void> {
    const store = await createRoomStore();
    this.store = store;

    store.subscribeState((state) => this.state.set(state));

    store.subscribeLabels((label) => {
      if (label.type === 'ERROR') {
        this.toast.add({ severity: 'error', detail: label.message ?? undefined });
      }
    });
  }

  private async ready(): Promise<RoomStoreJs> {
    await this.init();
    return this.store!;
  }
}
