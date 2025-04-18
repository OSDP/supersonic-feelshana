import LeftAvatar from '../CopilotAvatar';
import Message from '../Message';
import styles from './style.module.less';
import { AgentType } from '../../type';
import { isMobile } from '../../../utils/utils';

type Props = {
  id: string | number;
  currentAgent?: AgentType;
  onSendMsg: (value: string) => void;
  onSendMsgWithRecommend?: (example: string) => void;
};

const AgentTip: React.FC<Props> = ({ id,currentAgent, onSendMsg, onSendMsgWithRecommend }) => {
  if (!currentAgent) {
    return null;
  }
  return (
    <div className={styles.agentTip}>
      {!isMobile && <LeftAvatar />}
      <Message position="left" bubbleClassName={styles.agentTipMsg}>
        {
          (''+id).endsWith('-CouldNotAnswer') ?
              <div className={styles.title}>
                您的问题我已记录，努力学习中…<br/>
                或许您可以问：
              </div> :
              <div className={styles.title}>
                您好，智能助理【{currentAgent.name}】
                将与您对话，试着问：
              </div>
        }
        <div className={styles.content}>
          <div className={styles.examples}>
            {currentAgent.examples?.length > 0 ? (
              currentAgent.examples.map(example => (
                <div
                  key={example}
                  className={styles.example}
                  onClick={() => {
                    if (currentAgent?.chatAppConfig?.SMALL_TALK?.enable) {
                      onSendMsgWithRecommend?.(example);
                    } else {
                      onSendMsg(example);
                    }
                  }}
                >
                  “{example}”
                </div>
              ))
            ) : (
              <div className={styles.example}>{currentAgent.description}</div>
            )}
          </div>
        </div>
      </Message>
    </div>
  );
};

export default AgentTip;
