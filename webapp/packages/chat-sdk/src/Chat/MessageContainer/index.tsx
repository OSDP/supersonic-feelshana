import Text from '../components/Text';
import { memo, useCallback, useEffect, useState } from 'react';
import { isEqual } from 'lodash';
import { AgentType, MessageItem, MessageTypeEnum } from '../type';
import { isMobile, updateMessageContainerScroll } from '../../utils/utils';
import styles from './style.module.less';
import AgentTip from '../components/AgentTip';
import classNames from 'classnames';
import { MsgDataType } from '../../common/type';
import ChatItem from '../../components/ChatItem';

type Props = {
  id: string;
  chatId: number;
  messageList: MessageItem[];
  historyVisible: boolean;
  currentAgent?: AgentType;
  chatVisible?: boolean;
  isDeveloper?: boolean;
  integrateSystem?: string;
  isSimpleMode?: boolean;
  isDebugMode?: boolean;
  onMsgDataLoaded: (
    data: MsgDataType,
    questionId: string | number,
    question: string,
    valid: boolean,
    isRefresh?: boolean
  ) => void;
  onSendMsg: (value: string) => void;
  onCouldNotAnswer: () => void;
  fileResults: {
    fileContent: string;
    fileId: string;
    fileName: string;
    fileUid: string;
  }[]
};

const MessageContainer: React.FC<Props> = ({
  id,
  chatId,
  messageList,
  historyVisible,
  currentAgent,
  chatVisible,
  isDeveloper,
  integrateSystem,
  isSimpleMode,
  isDebugMode,
  onMsgDataLoaded,
  onSendMsg, onCouldNotAnswer,
  fileResults
                                           }) => {
  const [triggerResize, setTriggerResize] = useState(false);
  const onResize = useCallback(() => {
    setTriggerResize(true);
    setTimeout(() => {
      setTriggerResize(false);
    }, 0);
  }, []);

  useEffect(() => {
    window.addEventListener('resize', onResize);
    return () => {
      window.removeEventListener('resize', onResize);
    };
  }, []);

  useEffect(() => {
    onResize();
  }, [historyVisible, chatVisible]);

  const processMsg = (input) => {
    const regex = /文件\[([^\]]+)\]\s+文件id\[([^\]]+)\]\s+文件大小\[([^\]]+)\]\s+文件类型\[([^\]]+)\];\s+/g;
    const fileArr: JSX.Element[] = [];
    const text = input.replace(regex, (match, fileName, fileId, fileSize, fileType) => {
    const ext = fileName.split('.').pop().toLowerCase();
      let icon;
      if (['png', 'jpg', 'jpeg', 'gif', 'bmp', 'webp'].includes(ext)) {
        icon = '🖼️';
      } else if (['xls', 'xlsx', 'csv'].includes(ext)) {
        icon = '📊';
      } else if (['doc', 'docx'].includes(ext)) {
        icon = '📝';
      } else if (['ppt', 'pptx'].includes(ext)) {
        icon = '🎥';
      } else if (['txt', 'pdf', 'md', 'rtf'].includes(ext)) {
        icon = '📄';
      } else {
        icon = '📂';
      }
    
      const fileCard = (
        <div style={{display:'flex',justifyContent:'flex-end'}}>
          <div className={styles.fileItem}>
            <div className={styles.fileIcon}>
              {
                <span style={{fontSize:'24px'}}>&nbsp;{ icon }&nbsp;&nbsp;</span>
              }
            </div>
            <div className={styles.fileInfo}>
              <div className={styles.fileName}>{fileName}</div>
              <div className={styles.fileSize}>
                  {fileType + ' ' + fileSize}
              </div>
            </div>
          </div>
        </div>
      )
      fileArr.push(fileCard)
      return ''
    })
    return (
      <>
        { fileArr }
        <Text position="right" data={text} />
      </>
    )
  }

  const messageContainerClass = classNames(styles.messageContainer, { [styles.mobile]: isMobile });
  return (
    <div id={id} className={messageContainerClass}>
      <div className={styles.messageList}>
        {messageList.map((msgItem: MessageItem, index: number) => {
          const {
            id: msgId,
            questionId,
            modelId,
            agentId,
            type,
            msg,
            msgValue,
            score,
            identityMsg,
            parseInfos,
            parseTimeCost,
            msgData,
            filters,
          } = msgItem;

          return (
            <div key={msgId} id={`${msgId}`} className={styles.messageItem}>
              {type === MessageTypeEnum.TEXT && <Text position="left" data={msg} />}
              {type === MessageTypeEnum.AGENT_LIST && (
                  <AgentTip currentAgent={currentAgent} onSendMsg={onSendMsg} id={msgId}/>
              )}
              {type === MessageTypeEnum.QUESTION && (
                <>
                  {
                    currentAgent?.chatAppConfig?.SMALL_TALK?.enable ?
                    processMsg(msg):
                    <Text position="right" data={msg} />
                  }
                  {identityMsg && <Text position="left" data={identityMsg} />}
                  <ChatItem
                    msgId={msgId}
                    questionId={questionId}
                    currentAgent={currentAgent}
                    isSimpleMode={isSimpleMode}
                    isDebugMode={isDebugMode}
                    msg={msgValue || msg || ''}
                    parseInfos={parseInfos}
                    parseTimeCostValue={parseTimeCost}
                    msgData={msgData}
                    conversationId={chatId}
                    modelId={modelId}
                    agentId={agentId}
                    score={score}
                    filter={filters}
                    triggerResize={triggerResize}
                    isDeveloper={isDeveloper}
                    integrateSystem={integrateSystem}
                    onMsgDataLoaded={(data: MsgDataType, valid: boolean, isRefresh) => {
                      onMsgDataLoaded(data, msgId, msgValue || msg || '', valid, isRefresh);
                    }}
                    onUpdateMessageScroll={updateMessageContainerScroll}
                    onSendMsg={onSendMsg}
                    isLastMessage={index === messageList.length - 1}
                    onCouldNotAnswer={onCouldNotAnswer}
                    fileResults={fileResults}
                  />
                </>
              )}
            </div>
          );
        })}
      </div>
    </div>
  );
};

function areEqual(prevProps: Props, nextProps: Props) {
  if (
    prevProps.id === nextProps.id &&
    isEqual(prevProps.messageList, nextProps.messageList) &&
    prevProps.historyVisible === nextProps.historyVisible &&
    prevProps.currentAgent === nextProps.currentAgent &&
    prevProps.chatVisible === nextProps.chatVisible &&
    prevProps.isSimpleMode === nextProps.isSimpleMode
  ) {
    return true;
  }
  return false;
}

export default memo(MessageContainer, areEqual);
