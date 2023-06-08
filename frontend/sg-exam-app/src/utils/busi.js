export const setVideoSrc = (subject, $refs, timeout = 50) => {
  if (subject.subjectVideoId && subject.subjectVideoUrl) {
    setTimeout(() => {
      if ($refs.sgVideo !== undefined) {
        $refs.sgVideo.setSrc(subject.subjectVideoUrl)
      }
    }, timeout)
  }
}

export const pauseVideo = ($refs) => {
  if ($refs.sgVideo !== undefined) {
    $refs.sgVideo.pause()
  }
}

export const setAudioSrc = (subject, $refs, autoPlay, timeout = 50) => {
  if (subject.speechId && subject.speechUrl) {
    setTimeout(() => {
      if ($refs.sgAudio !== undefined) {
        $refs.sgAudio.setSrc(subject.speechUrl, autoPlay, subject.id)
      }
    }, timeout)
  }
}

export const pauseAudio = ($refs) => {
  if ($refs.sgAudio !== undefined) {
    $refs.sgAudio.pausePlay()
  }
}
