import { computed, inject, Injectable, signal } from '@angular/core';
import { MessageService } from 'primeng/api';
import { createHouseStore, type HouseStateJs, type HouseStoreJs } from 'shared';
import type { CreateHouseRequestDto } from '~/app/home/utils/house.dto';

@Injectable({ providedIn: 'root' })
export class HouseService {
  private readonly toast = inject(MessageService);
  private store: HouseStoreJs | null = null;
  private initPromise: Promise<void> | null = null;

  private readonly state = signal<HouseStateJs | null>(null);
  readonly loading = computed(() => this.state()?.loading ?? false);
  readonly house = computed(() => this.state()?.house ?? null);
  readonly houseId = computed(() => this.state()?.house?.id ?? null);
  readonly savedHouses = computed(() => this.state()?.savedHouses ?? []);
  readonly error = computed(() => this.state()?.error ?? null);

  /** Idempotent : peut être appelé plusieurs fois, le store n'est créé qu'une seule fois. */
  init(): Promise<void> {
    return (this.initPromise ??= this.doInit());
  }

  async getHouse(): Promise<void> {
    (await this.ready()).getHouse();
  }

  async createHouse(dto: CreateHouseRequestDto): Promise<void> {
    (await this.ready()).createHouse(dto.name);
  }

  async saveHouse(houseId: string): Promise<void> {
    (await this.ready()).saveHouse(houseId);
  }

  async clear(): Promise<void> {
    (await this.ready()).clear();
  }

  private async doInit(): Promise<void> {
    const store = await createHouseStore();
    this.store = store;

    store.subscribeState((state) => this.state.set(state));

    store.subscribeLabels((label) => {
      if (label.type === 'ERROR') {
        this.toast.add({ severity: 'error', detail: label.message ?? undefined });
      }
    });
  }

  private async ready(): Promise<HouseStoreJs> {
    await this.init();
    return this.store!;
  }
}
