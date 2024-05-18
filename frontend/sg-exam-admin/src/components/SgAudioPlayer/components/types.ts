export interface SgAudioPlayerOption {
  src: string //audio source
  title?: string //audio title
  coverImage?: string //cover image
  coverRotate?: boolean //cover rotate when playing
  progressBarColor?: string //progress bar color
  indicatorColor?: string //indicator color
}

export const AudioPlayerOptionDefault: SgAudioPlayerOption = {
  src: '',
  title: '',
  coverImage: '',
  coverRotate: true,
  progressBarColor: '#3C91F4',
  indicatorColor: '#3C91F4',
}
