package com.cn.coachs.ui.patient.main.healthdiary;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.cn.coachs.R;
import com.cn.coachs.model.nurse.BeanEditDiaryNode;
import com.cn.coachs.model.nurse.BeanEditDiaryNodeNoContent;
import com.cn.coachs.model.nurse.BeanEditDiaryNodeNoId;
import com.cn.coachs.ui.chat.common.base.CCPEditText;
import com.cn.coachs.ui.chat.common.base.CCPTextView;
import com.cn.coachs.ui.chat.ui.TabFragmentListView;
import com.cn.coachs.util.Constant;
import com.cn.coachs.util.CustomDialog;
import com.cn.coachs.util.UtilsSharedData;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * @author kuangtiecheng
 * @version 1.0
 * @date 创建时间：2015/10/13 下午5:13:01
 * @parameter
 * @return
 */
public class NightFrag extends TabFragmentListView implements OnClickListener {
    public View view;
    //	public ListView listview;
    public ListView check_listview;
    public EditDiaryDayItem one, two, three, four, five, six, seven, eight, nine, ten, eleven, twelve, thiteen;
    //	public CheckBox afternoon,sport,morning,sleep;
    public CCPEditText add_content;
    public CCPTextView no_content;
    public ArrayList<BeanEditDiaryNode> list;//模板长度
    public ArrayList<EditDiaryDayItem> listTextView;
    //	public ArrayList<CheckBox> listCheckBox;
    public BeanEditDiaryNode beanEditDiaryNode;
    public boolean flag = false;
    public String fansId;//即为后台的patientId
    public int num;
    //	public String[] models = {"监测午后血压心率","监测运动后血压心率","监测清晨血压心率","监测睡前血压心率"};
    public ArrayList<String> listContent;
    public String isMaster = "0";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = LayoutInflater.from(getActivity()).inflate(getLayoutId(), container, false);
        receiveData();
        init();
        return view;
    }

    public void receiveData() {
        list = this.getArguments().getParcelableArrayList(Constant.BEAN_EDIT_DIARY_NODE);
        fansId = list.get(0).getFansId();
        num = list.get(0).getDay();

        System.out.println("=-=-=传过来的list.size()=-=-=" + list.size());
        System.out.println("=-=-=传过来的list.get(0)=-=-=" + list.get(0));
    }

    public void init() {
        listTextView = new ArrayList<EditDiaryDayItem>();
//		listCheckBox = new ArrayList<CheckBox>();		

        one = (EditDiaryDayItem) view.findViewById(R.id.one);
        one.setOnClickListener(this);
        listTextView.add(one);

        two = (EditDiaryDayItem) view.findViewById(R.id.two);
        two.setOnClickListener(this);
        listTextView.add(two);

        three = (EditDiaryDayItem) view.findViewById(R.id.three);
        three.setOnClickListener(this);
        listTextView.add(three);

        four = (EditDiaryDayItem) view.findViewById(R.id.four);
        four.setOnClickListener(this);
        listTextView.add(four);

        five = (EditDiaryDayItem) view.findViewById(R.id.five);
        five.setOnClickListener(this);
        listTextView.add(five);

        six = (EditDiaryDayItem) view.findViewById(R.id.six);
        six.setOnClickListener(this);
        listTextView.add(six);

        seven = (EditDiaryDayItem) view.findViewById(R.id.seven);
        seven.setOnClickListener(this);
        listTextView.add(seven);

        eight = (EditDiaryDayItem) view.findViewById(R.id.eight);
        eight.setOnClickListener(this);
        listTextView.add(eight);

        nine = (EditDiaryDayItem) view.findViewById(R.id.nine);
        nine.setOnClickListener(this);
        listTextView.add(nine);

        ten = (EditDiaryDayItem) view.findViewById(R.id.ten);
        ten.setOnClickListener(this);
        listTextView.add(ten);

        eleven = (EditDiaryDayItem) view.findViewById(R.id.eleven);
        eleven.setOnClickListener(this);
        listTextView.add(eleven);

        initData(list, listTextView);
    }

    public void initData(ArrayList<BeanEditDiaryNode> list, ArrayList<EditDiaryDayItem> listTextView) {
        if (list.get(0).getId() != -1) {
            for (EditDiaryDayItem editDiaryDayItem : listTextView) {
                String time = editDiaryDayItem.getTime().replace(":", "");
                for (BeanEditDiaryNode beanEditDiaryNode : list) {
                    if (beanEditDiaryNode.getTime().equals(time)) {
                        editDiaryDayItem.setContent(beanEditDiaryNode.getContent().replaceAll("\n", "，"));
                    }
                }
            }
        } else
            return;
    }

    public BeanEditDiaryNode getBeanEditDiaryNode(EditDiaryDayItem item) {
        String time = item.getTime().replace(":", "");
        if (list.get(0).getId() != -1) {
            for (BeanEditDiaryNode beanEditDiaryNode : list) {
                if (beanEditDiaryNode.getTime().equals(time)) {
                    return beanEditDiaryNode;
                }
            }
        } else
            return list.get(0);
        return null;
    }

    @Override
    public void onTabFragmentClick() {
        // TODO Auto-generated method stub
        System.out.println("=-=-=onTabFragmentClick=-=-=");
    }

    @Override
    protected void onReleaseTabUI() {
        // TODO Auto-generated method stub
        System.out.println("=-=-=onReleaseTabUI=-=-=");

    }

    @Override
    protected int getLayoutId() {
        // TODO Auto-generated method stub
        return R.layout.activity_edit_diary_day_total_night;
    }

    @Override
    public void onClick(View arg0) {
        // TODO Auto-generated method stub
        super.onClick(arg0);
        switch (arg0.getId()) {
            case R.id.one:
                System.out.println("=-=-=one=-=-=");
                showChooseDialog(one);
                break;
            case R.id.two:
                System.out.println("=-=-=two=-=-=");
                showChooseDialog(two);
                break;
            case R.id.three:
                System.out.println("=-=-=three=-=-=");
                showChooseDialog(three);
                break;
            case R.id.four:
                System.out.println("=-=-=four=-=-=");
                showChooseDialog(four);
                break;
            case R.id.five:
                System.out.println("=-=-=five=-=-=");
                showChooseDialog(five);
                break;
            case R.id.six:
                System.out.println("=-=-=six=-=-=");
                showChooseDialog(six);
                break;
            case R.id.seven:
                System.out.println("=-=-=seven=-=-=");
                showChooseDialog(seven);
                break;
            case R.id.eight:
                System.out.println("=-=-=eight=-=-=");
                showChooseDialog(eight);
                break;
            case R.id.nine:
                System.out.println("=-=-=nine=-=-=");
                showChooseDialog(nine);
                break;
            case R.id.ten:
                System.out.println("=-=-=ten=-=-=");
                showChooseDialog(ten);
                break;
            case R.id.eleven:
                System.out.println("=-=-=eleven=-=-=");
                showChooseDialog(eleven);
                break;
            default:
                break;
        }
    }

    public void showChooseDialog(final EditDiaryDayItem item) {
        // TODO Auto-generated method stub
        if (isMaster.equals("0")) {
            Toast.makeText(getActivity(), R.string.youarenotmaster, Toast.LENGTH_LONG).show();
            return;
        }
        CustomDialog.Builder builder = new CustomDialog.Builder(getActivity());
        builder.setContentView(formChooseDialog());
        builder.setTitle("请选择方案项目");
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                BeanEditDiaryNode beanEditDiaryNode = getBeanEditDiaryNode(item);
                Log.e("=-=-=flag的值=-=-=", "" + flag);
                if (flag) {// 点击了不设项目
                    if (!(item.getContent().equals("")) && item.getContent() != null) {
                        if (beanEditDiaryNode != null) {
                            beanEditDiaryNode.setTime(item.getTime().replace(":", ""));
                            BeanEditDiaryNodeNoContent beanEditDiaryNodeNoContent = new BeanEditDiaryNodeNoContent();
                            beanEditDiaryNodeNoContent.setFansId(beanEditDiaryNode.getFansId());
                            beanEditDiaryNodeNoContent.setId(beanEditDiaryNode.getId());
                            beanEditDiaryNodeNoContent.setWeek(beanEditDiaryNode.getWeek());
                            beanEditDiaryNodeNoContent.setDay(beanEditDiaryNode.getDay());
                            beanEditDiaryNodeNoContent.setTime(beanEditDiaryNode.getTime());
                            Log.e("=-=-=beanEditDiaryNode的值=-=-=", "" + beanEditDiaryNode);
                            Log.e("=-=-=beanEditDiaryNodeNoContent的值=-=-=", "" + beanEditDiaryNodeNoContent);

                            AsynEditItemNoContent asynEditItemNoContent = new AsynEditItemNoContent(
                                    getActivity(), beanEditDiaryNodeNoContent) {
                                @Override
                                protected void onPostExecute(JSONObject result) {
                                    // TODO Auto-generated method stub
                                    super.onPostExecute(result);
                                    try {
                                        if (result.getInt("resultID") == 1) {
                                            item.setContent("");
                                            Toast.makeText(getActivity(), "修改成功", Toast.LENGTH_SHORT).show();
                                        } else
                                            Toast.makeText(getActivity(), "修改失败", Toast.LENGTH_SHORT).show();
                                    } catch (JSONException e) {
                                        // TODO Auto-generated catch block
                                        e.printStackTrace();
                                    }
                                }
                            };
                            asynEditItemNoContent.execute();
                        }
                    }
                } else {// 未点击不设项目
                    StringBuilder uploadContent = new StringBuilder();
                    System.out.println("=-=-=前  uploadContent=-=-=" + uploadContent);
//					for (CheckBox checkBox : listCheckBox) {
//						if (checkBox.isChecked())
//							uploadContent.append(checkBox.getText()).append("\n");
//					}
//					
//					if (!TextUtils.isEmpty(add_content.getText()))
//						uploadContent.append(add_content.getText().toString()).append("\n");

                    long[] checkedIds = check_listview.getCheckedItemIds();
                    for (int i = 0; i < checkedIds.length; i++) {
                        System.out.println("=-=-=checkedIds=-=-=" + checkedIds[i]);
                        if (listContent.size() > 0)
                            uploadContent.append(listContent.get((int) checkedIds[i])).append("\n");
                        else
                            Toast.makeText(getActivity(), "模板出问题了...快去找后台！", Toast.LENGTH_SHORT).show();
                    }
                    if (!TextUtils.isEmpty(add_content.getText()))
                        uploadContent.append(add_content.getText().toString()).append("\n");
                    System.out.println("=-=-=uploadContent的内容=-=-=" + uploadContent);

                    if (item.getContent().equals("") || item.getContent() == null) {// 添加
                        if (uploadContent.length() > 0 && list.size() > 0) {
                            BeanEditDiaryNode bean = list.get(0);
                            BeanEditDiaryNodeNoId beanEditDiaryNodeNoId = new BeanEditDiaryNodeNoId();
                            bean.setTime(item.getTime().replace(":", ""));
                            bean.setContent(uploadContent.deleteCharAt(uploadContent.length() - 1).toString());
                            beanEditDiaryNodeNoId.setContent(bean.getContent());
                            beanEditDiaryNodeNoId.setDay(bean.getDay());
                            beanEditDiaryNodeNoId.setFansId(bean.getFansId());
                            beanEditDiaryNodeNoId.setWeek(bean.getWeek());
                            beanEditDiaryNodeNoId.setTime(bean.getTime());
                            Log.e("=-=-=bean的值=-=-=", "" + bean);
                            if (beanEditDiaryNodeNoId.getContent() != null
                                    && !(beanEditDiaryNodeNoId.getContent().equals(""))) {
                                AsynEditItemNoId asynEditItemNoId = new AsynEditItemNoId(
                                        getActivity(), beanEditDiaryNodeNoId) {
                                    @Override
                                    protected void onPostExecute(
                                            JSONObject result) {
                                        // TODO Auto-generated method stub
                                        super.onPostExecute(result);
                                        try {
                                            if (result.getInt("resultID") == 1) {
                                                item.setContent(beanEditDiaryNode.getContent().replaceAll("\n", "，"));
                                                Toast.makeText(getActivity(), "修改成功", Toast.LENGTH_SHORT).show();
                                            } else
                                                Toast.makeText(getActivity(), "修改失败", Toast.LENGTH_SHORT).show();
                                        } catch (JSONException e) {
                                            // TODO Auto-generated catch block
                                            e.printStackTrace();
                                        }
                                    }
                                };
                                asynEditItemNoId.execute();
                            }
                        }
                    } else { // 修改原有的
                        System.out.println("=-=-=NightFrag  赋值后   beanEditDiaryNode=-=-=" + beanEditDiaryNode);
                        if (uploadContent.length() > 0) {
                            beanEditDiaryNode.setTime(item.getTime().replace(":", ""));
                            beanEditDiaryNode.setContent(uploadContent.deleteCharAt(uploadContent.length() - 1).toString());
                            Log.e("=-=-=beanEditDiaryNode的值=-=-=", "" + beanEditDiaryNode);
                            System.out.println("=-=-=NightFrag  赋值后   beanEditDiaryNode=-=-=" + beanEditDiaryNode);
                            if (!(item.getContent().equals("")) && item.getContent() != null) {// 编辑

                                if (beanEditDiaryNode.getContent() != null && !(beanEditDiaryNode.getContent().equals(""))) {
                                    AsynEditItem asynEditItem = new AsynEditItem(
                                            getActivity(), beanEditDiaryNode) {
                                        @Override
                                        protected void onPostExecute(JSONObject result) {
                                            // TODO Auto-generated method stub
                                            super.onPostExecute(result);
                                            try {
                                                if (result.getInt("resultID") == 1) {
                                                    item.setContent(beanEditDiaryNode.getContent().replaceAll("\n", "，"));
                                                    Toast.makeText(getActivity(), "修改成功", Toast.LENGTH_SHORT).show();
                                                } else
                                                    Toast.makeText(getActivity(), "修改失败", Toast.LENGTH_SHORT).show();
                                            } catch (JSONException e) {
                                                // TODO Auto-generated catch
                                                // block
                                                e.printStackTrace();
                                            }
                                        }
                                    };
                                    asynEditItem.execute();
                                }
                            }
                        }
                    }
                    if (uploadContent.length() > 0)
                        uploadContent.setLength(0);//初值

                }
                flag = false;//初值
//				for (CheckBox checkBox : listCheckBox) {//初值
//					checkBox.setChecked(false);
//				}
                refresh();
                dialog.dismiss();
            }
        });

        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.create().show();
    }

    @Override
    public void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
        showProgressBar();
        UtilsSharedData.initDataShare(getActivity());
        AsynGetOption asynGetOption = new AsynGetOption() {
            @Override
            protected void onPostExecute(JSONArray result) {
                // TODO Auto-generated method stub
                super.onPostExecute(result);
                listContent = new ArrayList<String>();
                for (int i = 0; i < result.length(); i++) {
                    try {
                        listContent.add(result.getString(i));
                        System.out.println("=-=-=listContent.get(0)=-=-=" + listContent.get(0));
                    } catch (JSONException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }
                AsyncIsFansMaster isfansmaster = new AsyncIsFansMaster(getActivity(), fansId) {
                    @Override
                    protected void onPostExecute(String result) {
                        // TODO Auto-generated method stub
                        super.onPostExecute(result);
                        hideProgressBar();
                        if (result != null) {
                            isMaster = result;
                        } else {
                            Toast.makeText(getActivity(), R.string.network_error, Toast.LENGTH_SHORT).show();
                            isMaster = "0";
                        }
                    }
                };
                isfansmaster.execute();
            }
        };
        asynGetOption.execute();
    }

    @Override
    public void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();
        listContent = null;
    }

    public View formChooseDialog() {
        // TODO Auto-generated method stub
        LayoutInflater inflater = LayoutInflater.from(getActivity());
        LinearLayout layout = (LinearLayout) inflater.inflate(R.layout.activity_edit_diary_time_content, null);

//		afternoon = (CheckBox)layout.findViewById(R.id.afternoon);
//		listCheckBox.add(afternoon);
//			
//		sport = (CheckBox)layout.findViewById(R.id.sport);
//		listCheckBox.add(sport);
//				
//		morning = (CheckBox)layout.findViewById(R.id.morning);
//		listCheckBox.add(morning);
//		
//		sleep = (CheckBox)layout.findViewById(R.id.sleep);
//		listCheckBox.add(sleep);

//		listContent = new ArrayList<String>();
//		for(int i = 0;i<models.length;i++){
//			listContent.add(models[i]);
//			System.out.println("=-=-=modelsContent.size()=-=-="+listContent.size());
//		}

        check_listview = (ListView) layout.findViewById(R.id.check_listview);
//		ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(),android.R.layout.simple_list_item_multiple_choice,models);
        MyMutiCheckedAdapter adapter = new MyMutiCheckedAdapter(listContent, getActivity());
        check_listview.setAdapter(adapter);

        add_content = (CCPEditText) layout.findViewById(R.id.add_content);
        add_content.setOnClickListener(click);
        setEditable(add_content, false);

        no_content = (CCPTextView) layout.findViewById(R.id.no_content);
        no_content.setOnClickListener(click);

        return layout;
    }

    /**
     * 由编辑状态切换到不可编辑状态
     */
    protected void setEditable(EditText editText, boolean value) {
        if (value) {
            getActivity();
            InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            editText.setFocusableInTouchMode(true);
            editText.requestFocus();
            imm.showSoftInput(editText, 0);
        } else {
            editText.setFocusableInTouchMode(false);
            editText.clearFocus();
        }
    }

    protected boolean clickUp() {
//		afternoon.setClickable(false);
//		afternoon.setChecked(false);
//		
//		sport.setClickable(false);
//		sport.setChecked(false);
//		
//		morning.setClickable(false);
//		morning.setChecked(false);
//		
//		sleep.setClickable(false);
//		sleep.setChecked(false);

        add_content.setText("");
        add_content.setEnabled(false);
        no_content.setBackgroundColor(getActivity().getResources().getColor(android.R.color.darker_gray));
        return true;
    }

    public OnClickListener click = new OnClickListener() {
        @Override
        public void onClick(View v) {
            // TODO Auto-generated method stub
            if (v.getId() == R.id.no_content) {
                flag = clickUp();
            } else if (v.getId() == R.id.add_content) {
                setEditable(add_content, true);
            }
        }
    };

    public void refresh() {
        AsynGetDiaryItem asynGetDiaryItem = new AsynGetDiaryItem(fansId, num) {
            @Override
            protected void onPostExecute(JSONArray result) {
                // TODO Auto-generated method stub
                super.onPostExecute(result);
                System.out.println("=-=-=AsynGetDiaryItem  result.length()=-=-=" + result.length());
                list = parseJSONArray(result, num);
            }
        };
        asynGetDiaryItem.execute();
    }

    public ArrayList<BeanEditDiaryNode> parseJSONArray(JSONArray result, int position) {
        ArrayList<BeanEditDiaryNode> list = new ArrayList<BeanEditDiaryNode>();
        for (int i = 0; i < result.length(); i++) {
            BeanEditDiaryNode beanEditDiaryNode = new BeanEditDiaryNode();
            try {
                beanEditDiaryNode.setId(result.getJSONObject(i).getLong("id"));
                beanEditDiaryNode.setContent(result.getJSONObject(i).getString("content"));
                beanEditDiaryNode.setTime(result.getJSONObject(i).getString("time"));
                beanEditDiaryNode.setDay(result.getJSONObject(i).getInt("day"));
                beanEditDiaryNode.setWeek(position / 7 + 1);
                beanEditDiaryNode.setFansId(fansId);
                System.out.println("=-=-=beanEditDiaryNode=-=-=" + beanEditDiaryNode.toString());
                list.add(beanEditDiaryNode);
            } catch (JSONException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        return list;
    }

}
