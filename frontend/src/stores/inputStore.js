import { defineStore } from 'pinia';

export const useInputStore = defineStore('inputStore', {
  state: () => ({
    inputs: {
      senderid: '',
      recieverid: '',
      recieveraccountnumber: '',
      recieveraccountbank: '',
      amount: '',
      step: '',
      name: '',
    },
  }),
  actions: {
    updateInput(key, value) {
      this.inputs[key] = value;
    },
  },
});
