package dokapon.lz.entities;

import dokapon.bps.sections.Data;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Node {

    int offset;
    int offsetRepeat = -1;
    int repeatLength = 0;
    String data;
    String pattern;
    Node repeatOf;
    NodeType type = NodeType.UNTREATED;

    Node left;
    Node right;
    Node parent;

    public Node(String data, Node parent) {
        this.data = data;
        this.parent = parent;
    }

    public List<CompressedByte> getCompressedByte(Header header) {
        List<CompressedByte> cbs = new ArrayList<>();
        if (offset==3652) {
            System.out.println();
        }
        if (left!=null) cbs.addAll(left.getCompressedByte(header));
        if (type== NodeType.DATA) cbs.add(new DataByte(Integer.parseInt(data, 16) & 0xFF));
        else if ((type== NodeType.REPEAT && (offsetRepeat==-1 || (offset / 2 - offsetRepeat / 2 - 1)>header.getMaxPosition())) || type== NodeType.UNTREATED) {
            for (int i=0;i<data.length();i=i+2) {
                cbs.add(new DataByte(Integer.parseInt(data.substring(i, i+2), 16) & 0xFF));
            }
        } else if (type== NodeType.REPEAT) {
            if (repeatLength>header.getMaxSize()) {
                int o = offset;
                int l = repeatLength;
                int position = -1;
                while (l>0) {
                    if (l==1) {
                        cbs.add(new DataByte(Integer.parseInt(data.substring(data.length()-2), 16) & 0xFF));
                        l--;
                    }
                    else {
                        if (position==-1) position = o / 2 - offsetRepeat / 2 - 1;
                        RepeatByte rb = new RepeatByte(header, (Math.min(l, header.getMaxSize())), position);
                        cbs.add(rb);
                        o = o + Math.min(l, header.getMaxSize());
                        l = l - header.getMaxSize();
                    }
                }
            }
            else {
                RepeatByte rb = new RepeatByte(header, repeatLength, offset / 2 - offsetRepeat / 2 - 1);
                cbs.add(rb);
            }
        }
        if (right!=null) cbs.addAll(right.getCompressedByte(header));
        return cbs;
    }

    public void println() {
        if (left!=null) left.println();
        System.out.println(
                String.format("%-6d",offset)
                +"\t"+String.format("%-9s",type)
                +"\t"+getRepeatLength()
                +"\t"+offsetRepeat
                        +"\t"+String.format("%-16s",data)
                        +"\t"+String.format("%-16s",pattern)
        );
        if (right!=null) right.println();
    }

    public void split(String pattern) {
        if (pattern.equals("7EFF")) {
            System.out.println();
        }
        if (offset==1834) {
            System.out.println();
        }
        int index = data.indexOf(pattern);
        while (index>=0 && index%2!=0) index = data.indexOf(pattern, index + 1);
        if (index>=0) {
            int i = index + pattern.length();
            while (data.indexOf(pattern, i) == i) {
                i = data.indexOf(pattern, i) + pattern.length();
            }
            String leftData = data.substring(0, index);
            String rightData = data.substring(i);
            String middleData = data.substring(index, i);
            //System.out.println("middleData="+middleData);
            setType(NodeType.REPEAT);
            setRepeatLength((middleData.length()-pattern.length())/2);
            if (middleData.equals(pattern)) setRepeatLength(pattern.length()/2);
            setPattern(pattern);
            if (!leftData.isEmpty()) {
                // Insert new node left
                Node newleft = new Node(leftData, this);
                newleft.setOffset(offset);
                if (left!=null) {
                    left.setParent(newleft);
                }
                newleft.setLeft(left);
                left = newleft;
                setOffset(offset+leftData.length());
            }
            if (!rightData.isEmpty()) {
                // Insert new node right
                Node newright = new Node(rightData, this);
                newright.setOffset(offset);
                if (right!=null) {
                    right.setParent(newright);
                }
                newright.setRight(right);
                right = newright;
                right.setOffset(offset+middleData.length());
            }
            if (i>index+pattern.length()) {
                Node newFutureDataNode = new Node(pattern, this);
                newFutureDataNode.setOffset(offset);
                newFutureDataNode.setType(NodeType.REPEAT);
                if (left!=null) {
                    left.setParent(newFutureDataNode);
                }
                newFutureDataNode.setLeft(left);
                newFutureDataNode.setRepeatLength(0);
                left = newFutureDataNode;
                setOffset(offset+pattern.length());
                data = data.substring(index+pattern.length(), i);
            } else {
                data = data.substring(index, i);
            }
            //setRepeatLength(((middleData.length()-pattern.length())/pattern.length())+1);
            if (left != null) {
                left.split(pattern);
            }
            //offset += index;
            if (right!=null) {
                right.split(pattern);
            }
        } else {
            if (left!=null) left.split(pattern);
            if (right!=null) right.split(pattern);
        }
    }

    public List<Node> getUntreatedNodes() {
        List<Node> nodes = new ArrayList<>();
        if (type== NodeType.UNTREATED) nodes.add(this);
        if (left!=null) nodes.addAll(left.getUntreatedNodes());
        if (right!=null) nodes.addAll(right.getUntreatedNodes());
        return nodes;
    }

    public int countType(NodeType nt) {
        int i = 0;
        if (type==nt) i = 1;
        return i + (left!=null?left.countType(nt):0) + (right!=null? right.countType(nt) : 0);
    }

    public int getTreeSize(NodeType nt) {
        int size = getSize();
        if (type!=nt) size = 0;
        return size + (left!=null?left.getTreeSize(nt):0) + (right!=null?right.getTreeSize(nt):0);
    }

    public Map<String, Integer> updateRepeat(Map<String, Integer> patternOffset) {
        if (offset==288) {
            System.out.println();
        }
        if (left!=null) patternOffset.putAll(left.updateRepeat(patternOffset));
        if (type== NodeType.REPEAT) {
            Integer found = patternOffset.get(pattern);
            if (found==null) {
                patternOffset.put(pattern, offset);
                /*if (data.matches("^(.)\\1*$")) {
                    compressRepeatingData();
                }*/
            } else {
                offsetRepeat = found.intValue();
                patternOffset.put(pattern, offset);
            }
        }
        if (right!=null) patternOffset.putAll(right.updateRepeat(patternOffset));
        return patternOffset;
    }

    private void compressRepeatingData() {
        Node dataNode = new Node(data.substring(0,2), this);
        dataNode.setType(NodeType.DATA);
        if (left!=null) {
            left.setParent(dataNode);
            dataNode.setLeft(left);
        }
        left = dataNode;
        dataNode.setOffset(offset);
        setOffset(offset+2);
        setRepeatOf(dataNode);
        setType(NodeType.REPEAT);
        setOffsetRepeat(dataNode.getOffset());
        setRepeatLength(data.length()/2-1);
    }

    public int getSize() {
        int size = data.length()/2;
        if (type== NodeType.REPEAT) size = 2;
        if (type== NodeType.DATA) size = 1;
        return size;
    }

    public int getTreeSize() {
        int size = data.length()/2;
        if (type== NodeType.REPEAT) size = 2;
        if (type== NodeType.DATA) size = 1;
        return size + (left!=null?left.getTreeSize():0) + (right!=null?right.getTreeSize():0);
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getPattern() {
        return pattern;
    }

    public void setPattern(String pattern) {
        this.pattern = pattern;
    }

    public Node getLeft() {
        return left;
    }

    public void setLeft(Node left) {
        this.left = left;
    }

    public Node getRight() {
        return right;
    }

    public void setRight(Node right) {
        this.right = right;
    }

    public int getOffset() {
        return offset;
    }

    public void setOffset(int offset) {
        this.offset = offset;
        if (offset==1834) {
            System.out.println();
        }
    }

    public Node getRepeatOf() {
        return repeatOf;
    }

    public void setRepeatOf(Node repeatOf) {
        this.repeatOf = repeatOf;
    }

    public NodeType getType() {
        return type;
    }

    public void setType(NodeType type) {
        this.type = type;
    }

    public String getTreeData() {
        return (left!=null? left.getTreeData():"")+getData()+(right!=null?right.getTreeData():"");
    }

    public Node getParent() {
        return parent;
    }

    public void setParent(Node parent) {
        this.parent = parent;
    }

    public int getOffsetRepeat() {
        return offsetRepeat;
    }

    public void setOffsetRepeat(int offsetRepeat) {
        this.offsetRepeat = offsetRepeat;
    }

    public int getRepeatLength() {
        return repeatLength;
    }

    public void setRepeatLength(int repeatLength) {
        this.repeatLength = repeatLength;
    }

    @Override
    public String toString() {
        return "Node{" +
                "offset=" + offset +
                ", offsetRepeat=" + offsetRepeat +
                ", data='" + data + '\'' +
                ", repeatOf=" + repeatOf +
                ", type=" + type +
                ", left=" + left +
                ", right=" + right +
                '}';
    }


}
