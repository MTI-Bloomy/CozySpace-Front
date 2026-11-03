import { computed, inject, Injectable, signal } from '@angular/core';
import { MessageService } from 'primeng/api';
import { Subject } from 'rxjs';
import { createRewardStore, type RewardStateJs, type RewardStoreJs } from 'shared';
import type { ChooseRewardRequestDto } from '~/app/home/utils/reward.dto';

@Injectable({ providedIn: 'root' })
export class RewardService {
  private readonly toast = inject(MessageService);
  private store: RewardStoreJs | null = null;
  private initPromise: Promise<void> | null = null;

  private readonly state = signal<RewardStateJs | null>(null);
  readonly loading = computed(() => this.state()?.loading ?? false);
  readonly rewards = computed(() => this.state()?.rewards ?? []);
  readonly error = computed(() => this.state()?.error ?? null);

  /** Émet quand un reward a été choisi : à écouter pour recharger les "todo done" non réclamés. */
  readonly refreshTodoDoneNotClaimed$ = new Subject<void>();

  /** Idempotent : peut être appelé plusieurs fois, le store n'est créé qu'une seule fois. */
  init(): Promise<void> {
    return (this.initPromise ??= this.doInit());
  }

  async getRewards(): Promise<void> {
    (await this.ready()).getRewards();
  }

  async getReward(id: string): Promise<void> {
    (await this.ready()).getReward(id);
  }

  async chooseReward(dto: ChooseRewardRequestDto): Promise<void> {
    (await this.ready()).chooseReward(dto.todoDoneId, dto.houseId, dto.roomId, dto.placement);
  }

  async clear(): Promise<void> {
    (await this.ready()).clear();
  }

  private async doInit(): Promise<void> {
    const store = await createRewardStore();
    this.store = store;

    store.subscribeState((state) => this.state.set(state));

    store.subscribeLabels((label) => {
      switch (label.type) {
        case 'ERROR':
          this.toast.add({ severity: 'error', detail: label.message ?? undefined });
          break;
        case 'REFRESH_TODO_DONE_NOT_CLAIMED':
          this.refreshTodoDoneNotClaimed$.next();
          break;
      }
    });
  }

  private async ready(): Promise<RewardStoreJs> {
    await this.init();
    return this.store!;
  }
}
