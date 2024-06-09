export class TimeOutFunctions{

  public static runWithTimeoutAction(durationMs: number, currentAction: () => void, timeoutAction: () => void): void {
    currentAction();
    setTimeout(() => {
        timeoutAction();
    }, durationMs);
  }

}