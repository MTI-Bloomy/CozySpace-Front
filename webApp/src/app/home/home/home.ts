import { Component, effect, inject, type OnInit, untracked } from '@angular/core';
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
  protected readonly houses = inject(HouseService);
  protected readonly rooms = inject(RoomService);
  protected readonly rewards = inject(RewardService);

  constructor() {
    // Dès qu'on connaît l'id de la maison courante, on charge ses pièces.
    effect(() => {
      const houseId = this.houses.houseId();
      if (houseId) {
        untracked(() => void this.rooms.getRooms(houseId));
      }
    });
  }

  ngOnInit(): void {
    void this.houses.getHouse();
    void this.rewards.getRewards();
  }

  protected refresh(): void {
    void this.houses.getHouse();
    void this.rewards.getRewards();

    const houseId = this.houses.houseId();
    if (houseId) {
      void this.rooms.getRooms(houseId);
    }
  }
}
