import { computed, inject, Injectable, signal } from '@angular/core';
import { MessageService } from 'primeng/api';
import { createRoomStore, type RoomStateJs, type RoomStoreJs } from 'shared';
import { RewardService } from './reward.service';

@Injectable({ providedIn: 'root' })
export class RoomService {
  private readonly toast = inject(MessageService);
  private store: RoomStoreJs | null = null;
  private initPromise: Promise<void> | null = null;

  private readonly state = signal<RoomStateJs | null>(null);
  readonly loading = computed(() => this.state()?.loading ?? false);
  readonly rooms = computed(() => this.state()?.rooms ?? []);
  readonly error = computed(() => this.state()?.error ?? null);

  private readonly rewardService = inject(RewardService);

  /** Idempotent : peut être appelé plusieurs fois, le store n'est créé qu'une seule fois. */
  init(): Promise<void> {
    return (this.initPromise ??= this.doInit());
  }

  async getRooms(houseId: string, saveMode = false): Promise<void> {
    (await this.ready()).getRooms(houseId, saveMode);
  }

  async placeItem(houseId: string, roomId: string, rewardId: string, saveMode = false) {
  (await this.ready()).placeItem(houseId, roomId, rewardId, saveMode);
}

async removeItem(houseId: string, roomId: string, rewardId: string, saveMode = false) {
  (await this.ready()).removeItem(houseId, roomId, rewardId, saveMode);
}

  async clear(): Promise<void> {
    (await this.ready()).clear();
  }

  private async doInit(): Promise<void> {
    const store = await createRoomStore();
    this.store = store;

    store.subscribeState((state) => this.state.set(state));

    store.subscribeLabels((label) => {
      switch (label.type) {
        case 'ERROR':
          this.toast.add({ severity: 'error', detail: label.message ?? undefined });
          break;
        case 'REFRESH_ROOM':
          label.rewards?.forEach((id) => void this.rewardService.getReward(id));
          break;
      }
    });
  }

  private async ready(): Promise<RoomStoreJs> {
    await this.init();
    return this.store!;
  }
}
