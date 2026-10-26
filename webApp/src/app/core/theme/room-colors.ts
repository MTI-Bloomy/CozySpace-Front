export type Room = 'kitchen' | 'bathroom' | 'bedroom' | 'garden' | 'work';

export interface BubbleColors {
  dark: string;
  light: string;
  transparent: string;
}

const bubble = (name: string): BubbleColors => ({
  dark: `var(--bubble-${name}-dark)`,
  light: `var(--bubble-${name}-light)`,
  transparent: `var(--bubble-${name}-transparent)`,
});

export const ROOM_COLORS: Record<Room, BubbleColors> = {
  kitchen: bubble('yellow'),
  bathroom: bubble('blue'),
  bedroom: bubble('red'),
  garden: bubble('green'),
  work: bubble('purple'),
};