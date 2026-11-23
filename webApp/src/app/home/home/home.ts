import { Component, computed, effect, inject, type OnInit, signal, untracked } from '@angular/core';
import { AuthService } from '@core/services/auth.service';
import { HouseService } from '@core/services/house.service';
import { RewardService } from '@core/services/reward.service';
import { RoomService } from '@core/services/room.service';

@Component({
  selector: 'app-home',
  imports: [],
  templateUrl: './home.html',
  styleUrl: './home.scss',
})
export class Home implements OnInit {
  protected readonly auth = inject(AuthService);
  protected readonly houses = inject(HouseService);
  protected readonly rooms = inject(RoomService);
  protected readonly rewards = inject(RewardService);
  protected readonly currentRoomIndex = signal(0);
  protected readonly currentRoom = computed(() => {
    const roomList = this.rooms.rooms();
    return roomList[this.currentRoomIndex()] ?? null;
  });
  protected readonly displayedRoom = computed(() => {
    const room = this.currentRoom();
    return room ? [room] : [];
  });
  protected readonly roomDirection = signal<'next' | 'previous'>('next');

  constructor() {
    effect(() => {
      const houseId = this.houses.houseId();
      if (houseId) {
        untracked(() => void this.rooms.getRooms(houseId));
      }
    });

    effect(() => {
      const roomCount = this.rooms.rooms().length;
      if (roomCount > 0 && this.currentRoomIndex() >= roomCount) {
        this.currentRoomIndex.set(0);
      }
    });
  }

  ngOnInit(): void {
    this.auth.getUser();
    void this.houses.getHouse();
    void this.rewards.getRewards();
  }

  protected showPreviousRoom(): void {
    const roomCount = this.rooms.rooms().length;
    if (roomCount > 0) {
      this.roomDirection.set('previous');
      this.currentRoomIndex.update((index) => (index - 1 + roomCount) % roomCount);
    }
  }

  protected showNextRoom(): void {
    const roomCount = this.rooms.rooms().length;
    if (roomCount > 0) {
      this.roomDirection.set('next');
      this.currentRoomIndex.update((index) => (index + 1) % roomCount);
    }
  }

  protected furnitureForCurrentRoom() {
    const room = this.currentRoom();
    if (!room) {
      return [];
    }

    return this.rewards.rewards().filter((reward) => room.furniture.includes(reward.id));
  }

  protected roomBackground(type: string): string {
    return type === 'WORK' ? 'assets/work_base.png' : 'assets/kitchen_base.png';
  }
}
