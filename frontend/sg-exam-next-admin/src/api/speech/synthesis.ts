import {
  SynthesisListItem,
  SynthesisListGetResultModel,
} from './model/systemModel';

import {defHttp} from '/@/utils/http/axios';
import {UserService} from '/@/api/services';

const Api = {
  Base: UserService + '/v1/speechSynthesis',
  SynthesisList: UserService + '/v1/speechSynthesis/speechSynthesisList',
}

export const getSpeechSynthesisList = (params?: SynthesisListItem) =>
  defHttp.get<SynthesisListGetResultModel>({url: Api.SynthesisList, params});

export const createSpeech = (params?: SynthesisListItem
) => {
  return defHttp.post<void>(
    {
      url: Api.Base,
      params,
    }
  );
};

export const updateSpeech = (id: string, params?: SynthesisListItem) => {
  return defHttp.put<void>(
    {
      url: Api.Base + '/' + id,
      params,
    }
  );
};

export const deleteSpeech = (id: string) => {
  return defHttp.delete<void>(
    {
      url: Api.Base + '/' + id,
    }
  );
};


